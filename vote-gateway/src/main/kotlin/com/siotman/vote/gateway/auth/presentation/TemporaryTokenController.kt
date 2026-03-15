package com.siotman.vote.gateway.auth.presentation

import com.nimbusds.jose.JOSEException
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.MACSigner
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import com.siotman.vote.gateway.auth.application.IdentityTranslationException
import com.siotman.vote.gateway.auth.application.UnsupportedIssuerException
import com.siotman.vote.gateway.auth.config.IdentityTranslationProperties
import com.siotman.vote.gateway.common.api.ApiResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import java.util.Date
import java.util.UUID

@RestController
@RequestMapping("/auth/token")
class TemporaryTokenController(
    private val properties: IdentityTranslationProperties,
) {
    @PostMapping
    fun issueToken(
        @RequestBody request: IssueTokenRequest,
    ): ApiResponse<IssueTokenResponse> {
        val issuer = properties.issuers.firstOrNull { it.issuer == request.issuer }
            ?: throw UnsupportedIssuerException(request.issuer)

        val issuedAt = Instant.now()
        val expiresAt = issuedAt.plusSeconds(request.expiresInSeconds)

        val claims = JWTClaimsSet.Builder()
            .issuer(issuer.issuer)
            .subject(request.subject ?: request.principalId)
            .issueTime(Date.from(issuedAt))
            .expirationTime(Date.from(expiresAt))
            .jwtID(UUID.randomUUID().toString())
            .claim(issuer.principalClaim, request.principalId)
            .apply {
                request.accountId?.let { claim(issuer.accountIdClaim, it) }
                request.tenantId?.let { claim(issuer.tenantIdClaim, it) }
                if (request.roles.isNotEmpty()) {
                    claim(issuer.rolesClaim, request.roles)
                }
                if (request.scopes.isNotEmpty()) {
                    claim(issuer.scopesClaim, request.scopes)
                }
            }
            .build()

        return ApiResponse.success(
            IssueTokenResponse(
                accessToken = sign(claims, issuer),
                tokenType = "Bearer",
                expiresAt = expiresAt,
            ),
        )
    }

    private fun sign(
        claims: JWTClaimsSet,
        issuer: IdentityTranslationProperties.TrustedIssuer,
    ): String {
        val signer = try {
            MACSigner(issuer.secret.toByteArray())
        } catch (exception: JOSEException) {
            throw IdentityTranslationException("JWT 서명을 초기화할 수 없습니다.", exception)
        }
        val jwt = SignedJWT(
            JWSHeader.Builder(issuer.algorithm.jwsAlgorithm).build(),
            claims,
        )
        try {
            jwt.sign(signer)
        } catch (exception: JOSEException) {
            throw IdentityTranslationException("JWT 서명에 실패했습니다.", exception)
        }
        return jwt.serialize()
    }
}

data class IssueTokenRequest(
    val issuer: String = "local-dev",
    val principalId: String,
    val subject: String? = null,
    val accountId: String? = null,
    val tenantId: String? = null,
    val roles: Set<String> = emptySet(),
    val scopes: Set<String> = emptySet(),
    val expiresInSeconds: Long = 3600,
)

data class IssueTokenResponse(
    val accessToken: String,
    val tokenType: String,
    val expiresAt: Instant,
)
