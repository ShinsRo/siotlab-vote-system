package com.siotman.vote.gateway.auth.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "identity.translation")
data class IdentityTranslationProperties(
    val headers: Map<IdentityHeaderName, String> = emptyMap(),
    val publicPathPrefixes: List<String> = listOf("/mgmt", "/auth/token"),
    val forwardAuthorizationHeader: Boolean = false,
    val issuers: List<TrustedIssuer> = emptyList(),
) {
    data class TrustedIssuer(
        val issuer: String,
        val secret: String,
        val algorithm: SignatureAlgorithm = SignatureAlgorithm.HS256,
        val principalClaim: String = "sub",
        val subjectClaim: String = "sub",
        val accountIdClaim: String? = "account_id",
        val rolesClaim: String = "roles",
        val scopesClaim: String = "scope",
        val tenantIdClaim: String? = "tenant_id",
        val adminRoleNames: Set<String> = setOf("admin"),
    )
}

enum class IdentityHeaderName(
    val defaultHeaderName: String,
) {
    PRINCIPAL_ID("X-Identity-Principal-Id"),
    ACCOUNT_ID("X-Identity-Account-Id"),
    ISSUER("X-Identity-Issuer"),
    SUBJECT("X-Identity-Subject"),
    ROLES("X-Identity-Roles"),
    SCOPES("X-Identity-Scopes"),
    TENANT_ID("X-Identity-Tenant-Id"),
    TOKEN_ID("X-Identity-Token-Id"),
    ISSUED_AT("X-Identity-Issued-At"),
    EXPIRES_AT("X-Identity-Expires-At"),
}

fun IdentityTranslationProperties.headerName(name: IdentityHeaderName): String =
    headers[name] ?: name.defaultHeaderName

enum class SignatureAlgorithm(
    val macAlgorithm: org.springframework.security.oauth2.jose.jws.MacAlgorithm,
    val jwsAlgorithm: com.nimbusds.jose.JWSAlgorithm,
    val keyAlgorithm: String,
) {
    HS256(
        macAlgorithm = org.springframework.security.oauth2.jose.jws.MacAlgorithm.HS256,
        jwsAlgorithm = com.nimbusds.jose.JWSAlgorithm.HS256,
        keyAlgorithm = "HmacSHA256",
    ),
    HS384(
        macAlgorithm = org.springframework.security.oauth2.jose.jws.MacAlgorithm.HS384,
        jwsAlgorithm = com.nimbusds.jose.JWSAlgorithm.HS384,
        keyAlgorithm = "HmacSHA384",
    ),
    HS512(
        macAlgorithm = org.springframework.security.oauth2.jose.jws.MacAlgorithm.HS512,
        jwsAlgorithm = com.nimbusds.jose.JWSAlgorithm.HS512,
        keyAlgorithm = "HmacSHA512",
    ),
}
