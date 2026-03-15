package com.siotman.vote.gateway.auth.application

import com.siotman.vote.gateway.auth.config.IdentityTranslationProperties
import com.siotman.vote.gateway.auth.config.IdentityHeaderName
import com.siotman.vote.gateway.auth.config.headerName
import com.siotman.vote.gateway.auth.domain.TranslatedIdentity
import java.time.format.DateTimeFormatter

/**
 * 다운스트림으로 전달할 표준 헤더 집합이다.
 * 컬렉션 값은 쉼표로 직렬화하고, 시간 값은 ISO-8601 UTC 문자열로 내린다.
 */
data class TranslatedIdentityHeaders(
    val values: Map<String, String>,
) {
    companion object {
        fun from(
            identity: TranslatedIdentity,
            properties: IdentityTranslationProperties,
        ): TranslatedIdentityHeaders {
            val headers = linkedMapOf(
                properties.headerName(IdentityHeaderName.PRINCIPAL_ID) to identity.principalId,
                properties.headerName(IdentityHeaderName.ISSUER) to identity.issuer,
                properties.headerName(IdentityHeaderName.SUBJECT) to identity.subject,
            )

            identity.accountId?.let { headers[properties.headerName(IdentityHeaderName.ACCOUNT_ID)] = it }
            if (identity.roles.isNotEmpty()) {
                headers[properties.headerName(IdentityHeaderName.ROLES)] = identity.roles.joinToString(",") { it.name }
            }
            if (identity.scopes.isNotEmpty()) {
                headers[properties.headerName(IdentityHeaderName.SCOPES)] = identity.scopes.joinToString(",")
            }
            identity.tenantId?.let { headers[properties.headerName(IdentityHeaderName.TENANT_ID)] = it }
            identity.tokenId?.let { headers[properties.headerName(IdentityHeaderName.TOKEN_ID)] = it }
            identity.issuedAt?.let { headers[properties.headerName(IdentityHeaderName.ISSUED_AT)] = DateTimeFormatter.ISO_INSTANT.format(it) }
            identity.expiresAt?.let { headers[properties.headerName(IdentityHeaderName.EXPIRES_AT)] = DateTimeFormatter.ISO_INSTANT.format(it) }

            return TranslatedIdentityHeaders(values = headers)
        }

        fun allHeaderNames(properties: IdentityTranslationProperties): Set<String> =
            IdentityHeaderName.entries.mapTo(linkedSetOf()) { properties.headerName(it) }
    }
}
