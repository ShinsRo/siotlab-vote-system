@file:Suppress("NonAsciiCharacters")

package com.siotman.vote.gateway

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
abstract class AbstractGatewayIntegrationTest {
    @LocalServerPort
    protected var port: Int = 0

    protected val webTestClient: WebTestClient
        get() = WebTestClient.bindToServer().baseUrl("http://localhost:$port").build()
}
