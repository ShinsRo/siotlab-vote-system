@file:Suppress("NonAsciiCharacters")

package com.siotman.vote.gateway

import com.siotman.vote.gateway.auth.presentation.IssueTokenRequest
import org.junit.jupiter.api.Test

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
}
