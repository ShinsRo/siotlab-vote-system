@file:Suppress("NonAsciiCharacters")

package com.siotman.vote.adm

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class VoteAdmSpringApplicationTest {
    @LocalServerPort
    var port: Int = 0

    private val webTestClient: WebTestClient
        get() = WebTestClient.bindToServer().baseUrl("http://localhost:$port").build()

    @Test
    fun `애플리케이션 컨텍스트가 로드된다`() {
    }

    @Test
    fun `principal 헤더가 없으면 adm 요청이 거부된다`() {
        webTestClient.post()
            .uri("/api/v1/adm/campaigns")
            .bodyValue(
                mapOf(
                    "name" to "캠페인",
                    "description" to "설명",
                    "startAt" to "2026-03-15T09:00:00",
                    "endAt" to "2026-03-20T18:00:00",
                ),
            )
            .exchange()
            .expectStatus().isUnauthorized
            .expectBody()
            .jsonPath("$.error.code").isEqualTo("missing_principal_id")
    }

    @Test
    fun `admin 역할이 없으면 adm 요청이 거부된다`() {
        webTestClient.post()
            .uri("/api/v1/adm/campaigns")
            .header("X-Identity-Principal-Id", "101")
            .header("X-Identity-Roles", "USER")
            .bodyValue(
                mapOf(
                    "name" to "캠페인",
                    "description" to "설명",
                    "startAt" to "2026-03-15T09:00:00",
                    "endAt" to "2026-03-20T18:00:00",
                ),
            )
            .exchange()
            .expectStatus().isForbidden
            .expectBody()
            .jsonPath("$.error.code").isEqualTo("forbidden")
    }
}
