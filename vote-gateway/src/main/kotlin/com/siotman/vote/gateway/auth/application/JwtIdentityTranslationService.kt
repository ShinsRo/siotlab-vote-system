package com.siotman.vote.gateway.auth.application

import com.nimbusds.jwt.JWTParser
import com.siotman.vote.gateway.auth.config.IdentityTranslationProperties
import com.siotman.vote.gateway.auth.domain.IdentityRole
import com.siotman.vote.gateway.auth.domain.TranslatedIdentity
import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jwt.BadJwtException
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.stereotype.Service
import javax.crypto.spec.SecretKeySpec

@Service
class JwtIdentityTranslationService(
    private val properties: IdentityTranslationProperties,
) {
    private val decoders: Map<String, JwtDecoder> = properties.issuers.associate { issuer ->
        issuer.issuer to createDecoder(issuer)
    }

    fun translate(token: String): TranslatedIdentity {
        val issuer = extractIssuer(token)
        val issuerProperties = properties.issuers.firstOrNull { it.issuer == issuer }
            ?: throw UnsupportedIssuerException(issuer)

        val jwt = try {
            decoders.getValue(issuer).decode(token)
        } catch (exception: Exception) {
            throw IdentityTranslationException("JWT 검증에 실패했습니다.", exception)
        }

        return TranslatedIdentity(
            principalId = readRequiredClaim(jwt, issuerProperties.principalClaim),
            accountId = readOptionalClaim(jwt, issuerProperties.accountIdClaim),
            issuer = issuer,
            subject = readRequiredClaim(jwt, issuerProperties.subjectClaim),
            roles = normalizeRoles(jwt, issuerProperties),
            scopes = normalizeScopes(jwt, issuerProperties),
            tenantId = readOptionalClaim(jwt, issuerProperties.tenantIdClaim),
            tokenId = jwt.id,
            issuedAt = jwt.issuedAt,
            expiresAt = jwt.expiresAt,
        )
    }

    private fun createDecoder(issuer: IdentityTranslationProperties.TrustedIssuer): JwtDecoder {
        val keySpec = SecretKeySpec(issuer.secret.toByteArray(), issuer.algorithm.keyAlgorithm)
        return NimbusJwtDecoder.withSecretKey(keySpec)
            .macAlgorithm(issuer.algorithm.macAlgorithm)
            .build()
    }

    private fun extractIssuer(token: String): String {
        val claims = try {
            JWTParser.parse(token).jwtClaimsSet
        } catch (exception: Exception) {
            throw BadJwtException("JWT 파싱에 실패했습니다.", exception)
        }
        return claims.issuer ?: throw IdentityTranslationException("iss 클레임이 없습니다.")
    }

    private fun readRequiredClaim(jwt: Jwt, claimName: String): String =
        readOptionalClaim(jwt, claimName) ?: throw IdentityTranslationException("필수 클레임이 없습니다: $claimName")

    private fun readOptionalClaim(jwt: Jwt, claimName: String?): String? {
        if (claimName.isNullOrBlank()) {
            return null
        }
        return jwt.claims[claimName]?.toString()?.takeIf { it.isNotBlank() }
    }

    private fun normalizeRoles(
        jwt: Jwt,
        issuer: IdentityTranslationProperties.TrustedIssuer,
    ): Set<IdentityRole> {
        val rawRoles = readStringValues(jwt, issuer.rolesClaim)
        return rawRoles.mapNotNullTo(linkedSetOf()) { role ->
            when {
                issuer.adminRoleNames.any { adminRole -> adminRole.equals(role, ignoreCase = true) } -> IdentityRole.ADMIN
                else -> IdentityRole.USER
            }
        }
    }

    private fun normalizeScopes(
        jwt: Jwt,
        issuer: IdentityTranslationProperties.TrustedIssuer,
    ): Set<String> = readStringValues(jwt, issuer.scopesClaim)

    private fun readStringValues(jwt: Jwt, claimName: String): Set<String> {
        val claimValue = jwt.claims[claimName] ?: return emptySet()
        return when (claimValue) {
            is String -> claimValue.split(",", " ").map(String::trim).filter(String::isNotBlank).toSet()
            is Collection<*> -> claimValue.mapNotNull { it?.toString()?.trim() }.filter(String::isNotBlank).toSet()
            else -> setOf(claimValue.toString())
        }
    }
}

private val Jwt.id: String?
    get() = claims["jti"]?.toString()
