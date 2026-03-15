@file:Suppress("NonAsciiCharacters")

package com.siotman.vote.gateway.auth.application

import com.siotman.vote.gateway.auth.config.IdentityTranslationProperties
import com.siotman.vote.gateway.auth.config.IdentityHeaderName
import com.siotman.vote.gateway.auth.config.headerName
import com.siotman.vote.gateway.auth.domain.IdentityRole
import com.siotman.vote.gateway.auth.domain.TranslatedIdentity
import com.siotman.vote.gateway.auth.presentation.IssueTokenRequest
import com.siotman.vote.gateway.auth.presentation.TemporaryTokenController
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Instant

class JwtIdentityTranslationServiceTest {
    private val properties = IdentityTranslationProperties(
        issuers = listOf(
            IdentityTranslationProperties.TrustedIssuer(
                issuer = "local-dev",
                secret = "local-dev-signing-secret-local-dev-signing-secret",
                principalClaim = "uid",
                subjectClaim = "sub",
                accountIdClaim = "account_id",
                rolesClaim = "roles",
                scopesClaim = "scope",
                tenantIdClaim = "tenant_id",
                adminRoleNames = setOf("admin"),
            ),
        ),
    )

    private val tokenController = TemporaryTokenController(properties)
    private val service = JwtIdentityTranslationService(properties)

    @Test
    fun `issuer 클레임을 내부 표준 identity로 정규화한다`() {
        val token = tokenController.issueToken(
            IssueTokenRequest(
                principalId = "101",
                subject = "issuer-user-1",
                accountId = "account-1",
                tenantId = "tenant-1",
                roles = setOf("admin", "member"),
                scopes = setOf("vote:read", "vote:write"),
            ),
        ).data!!.accessToken

        val identity = service.translate(token)

        assertThat(identity.principalId).isEqualTo("101")
        assertThat(identity.subject).isEqualTo("issuer-user-1")
        assertThat(identity.accountId).isEqualTo("account-1")
        assertThat(identity.tenantId).isEqualTo("tenant-1")
        assertThat(identity.roles).contains(IdentityRole.ADMIN, IdentityRole.USER)
        assertThat(identity.scopes).containsExactlyInAnyOrder("vote:read", "vote:write")
    }

    @Test
    fun `헤더 이름은 enum 기본값을 사용하고 설정으로 override할 수 있다`() {
        val defaultProperties = IdentityTranslationProperties()
        val overrideProperties = IdentityTranslationProperties(
            headers = mapOf(
                IdentityHeaderName.PRINCIPAL_ID to "X-Principal-Id",
                IdentityHeaderName.ROLES to "X-Role-Set",
            ),
        )

        assertThat(defaultProperties.headerName(IdentityHeaderName.PRINCIPAL_ID))
            .isEqualTo("X-Identity-Principal-Id")
        assertThat(defaultProperties.headerName(IdentityHeaderName.ROLES))
            .isEqualTo("X-Identity-Roles")
        assertThat(overrideProperties.headerName(IdentityHeaderName.PRINCIPAL_ID))
            .isEqualTo("X-Principal-Id")
        assertThat(overrideProperties.headerName(IdentityHeaderName.ROLES))
            .isEqualTo("X-Role-Set")
    }

    @Test
    fun `직렬화 헤더 목록은 enum 전체를 기준으로 생성된다`() {
        val properties = IdentityTranslationProperties(
            headers = mapOf(
                IdentityHeaderName.PRINCIPAL_ID to "X-Principal-Id",
            ),
        )

        val headerNames = TranslatedIdentityHeaders.allHeaderNames(properties)

        assertThat(headerNames).containsExactlyInAnyOrder(
            "X-Principal-Id",
            "X-Identity-Account-Id",
            "X-Identity-Issuer",
            "X-Identity-Subject",
            "X-Identity-Roles",
            "X-Identity-Scopes",
            "X-Identity-Tenant-Id",
            "X-Identity-Token-Id",
            "X-Identity-Issued-At",
            "X-Identity-Expires-At",
        )
    }

    @Test
    fun `정규화 identity 는 헤더 이름 override를 반영해 직렬화된다`() {
        val properties = IdentityTranslationProperties(
            headers = mapOf(
                IdentityHeaderName.PRINCIPAL_ID to "X-Principal-Id",
                IdentityHeaderName.ROLES to "X-Role-Set",
            ),
        )

        val headers = TranslatedIdentityHeaders.from(
            identity = TranslatedIdentity(
                principalId = "101",
                issuer = "local-dev",
                subject = "issuer-user-1",
                roles = setOf(IdentityRole.ADMIN),
                scopes = setOf("vote:read"),
                issuedAt = Instant.parse("2026-03-16T00:00:00Z"),
            ),
            properties = properties,
        )

        assertThat(headers.values).containsEntry("X-Principal-Id", "101")
        assertThat(headers.values).containsEntry("X-Role-Set", "ADMIN")
        assertThat(headers.values).containsEntry("X-Identity-Issuer", "local-dev")
        assertThat(headers.values).containsEntry("X-Identity-Issued-At", "2026-03-16T00:00:00Z")
    }
}
