package com.siotman.vote.adm.common.web

import com.fasterxml.jackson.databind.ObjectMapper
import com.siotman.vote.core.common.api.ApiResponse
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class AdminAuthorizationFilter(
    private val objectMapper: ObjectMapper,
) : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val path = exchange.request.path.value()
        if (PUBLIC_PATH_PREFIXES.any { path.startsWith(it) }) {
            return chain.filter(exchange)
        }

        val principalId = exchange.request.headers.getFirst(PRINCIPAL_ID_HEADER)
        if (principalId.isNullOrBlank()) {
            return writeError(
                exchange = exchange,
                status = HttpStatus.UNAUTHORIZED,
                code = "missing_principal_id",
                message = "$PRINCIPAL_ID_HEADER 헤더가 필요합니다.",
            )
        }

        val rolesHeader = exchange.request.headers.getFirst(ROLES_HEADER)
        if (rolesHeader.isNullOrBlank()) {
            return writeError(
                exchange = exchange,
                status = HttpStatus.UNAUTHORIZED,
                code = "missing_roles",
                message = "$ROLES_HEADER 헤더가 필요합니다.",
            )
        }

        val roles = rolesHeader.split(",").map(String::trim).filter(String::isNotBlank).toSet()
        if (ADMIN_ROLE !in roles) {
            return writeError(
                exchange = exchange,
                status = HttpStatus.FORBIDDEN,
                code = "forbidden",
                message = "관리자 권한이 필요합니다.",
            )
        }

        return chain.filter(exchange)
    }

    private fun writeError(
        exchange: ServerWebExchange,
        status: HttpStatus,
        code: String,
        message: String,
    ): Mono<Void> {
        exchange.response.statusCode = status
        exchange.response.headers.contentType = MediaType.APPLICATION_JSON
        val body = objectMapper.writeValueAsBytes(ApiResponse.failure(code = code, message = message))
        val buffer = exchange.response.bufferFactory().wrap(body)
        return exchange.response.writeWith(Mono.just(buffer))
    }

    private companion object {
        const val ROLES_HEADER = "X-Identity-Roles"
        const val ADMIN_ROLE = "ADMIN"
        val PUBLIC_PATH_PREFIXES = listOf("/mgmt", "/swagger-ui", "/v3/api-docs")
    }
}
