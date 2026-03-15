@file:Suppress("NonAsciiCharacters")

package com.siotman.vote.gateway

import com.siotman.vote.gateway.auth.presentation.IssueTokenRequest
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicReference

class GatewayIdentityTranslationFilterTest : AbstractGatewayIntegrationTest() {
    @Test
    fun `프록시 요청은 bearer 토큰이 없으면 거부된다`() {
        webTestClient.get()
            .uri("/vote-api/policies")
            .exchange()
            .expectStatus().isUnauthorized
            .expectBody()
            .jsonPath("$.success").isEqualTo(false)
            .jsonPath("$.error.code").isEqualTo("missing_authorization")
    }

    @Test
    fun `임시 토큰 발급 엔드포인트는 공개된다`() {
        webTestClient.post()
            .uri("/auth/token")
            .bodyValue(
                IssueTokenRequest(
                    issuer = "local-dev",
                    principalId = "principal-1",
                    roles = setOf("admin"),
                ),
            )
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.success").isEqualTo(true)
            .jsonPath("$.data.accessToken").isNotEmpty
            .jsonPath("$.data.tokenType").isEqualTo("Bearer")
    }

    @Test
    fun `관리자 역할이 없으면 adm 프록시 요청이 거부된다`() {
        val accessToken = issueToken(
            IssueTokenRequest(
                issuer = "local-dev",
                principalId = "101",
                subject = "issuer-user-1",
                roles = setOf("user"),
            ),
        )

        webTestClient.post()
            .uri("/vote-adm/api/v1/adm/campaigns")
            .header("Authorization", "Bearer $accessToken")
            .bodyValue(
                mapOf(
                    "name" to "관리자 캠페인",
                    "description" to "권한 체크 테스트",
                    "startAt" to "2026-03-15T09:00:00",
                    "endAt" to "2026-03-20T18:00:00",
                ),
            )
            .exchange()
            .expectStatus().isForbidden
            .expectBody()
            .jsonPath("$.success").isEqualTo(false)
            .jsonPath("$.error.code").isEqualTo("forbidden")
    }

    private fun issueToken(request: IssueTokenRequest): String {
        val accessToken = AtomicReference<String>()

        webTestClient.post()
            .uri("/auth/token")
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.data.accessToken").value<String>(accessToken::set)

        return accessToken.get() ?: throw IllegalStateException("access token 응답 파싱에 실패했습니다.")
    }
}
