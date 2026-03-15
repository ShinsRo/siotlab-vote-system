package com.siotman.vote.gateway.auth.infrastructure.gateway

import com.siotman.vote.gateway.auth.application.IdentityTranslationException
import com.siotman.vote.gateway.auth.application.JwtIdentityTranslationService
import com.siotman.vote.gateway.auth.application.TranslatedIdentityHeaders
import com.siotman.vote.gateway.auth.config.IdentityTranslationProperties
import com.siotman.vote.gateway.common.api.ApiResponse
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.core.Ordered
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class IdentityTranslationFilter(
    private val jwtIdentityTranslationService: JwtIdentityTranslationService,
    private val identityTranslationProperties: IdentityTranslationProperties,
    private val objectMapper: ObjectMapper,
) : GlobalFilter, Ordered {
    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        val path = exchange.request.path.value()
        if (identityTranslationProperties.publicPathPrefixes.any { path.startsWith(it) }) {
            return chain.filter(exchange)
        }

        val authorization = exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION)
            ?: return unauthorized(exchange, "missing_authorization")
        if (!authorization.startsWith(BEARER_PREFIX, ignoreCase = true)) {
            return unauthorized(exchange, "invalid_authorization_scheme")
        }

        val token = authorization.removePrefix(BEARER_PREFIX).trim()
        if (token.isBlank()) {
            return unauthorized(exchange, "blank_access_token")
        }

        val translatedIdentity = try {
            jwtIdentityTranslationService.translate(token)
        } catch (_: IdentityTranslationException) {
            return unauthorized(exchange, "invalid_access_token")
        }

        val identityHeaders = TranslatedIdentityHeaders.from(
            identity = translatedIdentity,
            properties = identityTranslationProperties,
        )

        val mutatedRequest = exchange.request.mutate().headers { headers ->
            TranslatedIdentityHeaders.allHeaderNames(identityTranslationProperties).forEach(headers::remove)
            if (!identityTranslationProperties.forwardAuthorizationHeader) {
                headers.remove(HttpHeaders.AUTHORIZATION)
            }
            identityHeaders.values.forEach(headers::set)
        }.build()

        return chain.filter(exchange.mutate().request(mutatedRequest).build())
    }

    override fun getOrder(): Int = -100

    private fun unauthorized(exchange: ServerWebExchange, code: String): Mono<Void> {
        exchange.response.statusCode = HttpStatus.UNAUTHORIZED
        exchange.response.headers.contentType = MediaType.APPLICATION_JSON
        val response = ApiResponse.failure(code = code, message = "인증에 실패했습니다.")
        val buffer = exchange.response.bufferFactory().wrap(objectMapper.writeValueAsBytes(response))
        return exchange.response.writeWith(Mono.just(buffer))
    }

    private companion object {
        const val BEARER_PREFIX = "Bearer "
    }
}
