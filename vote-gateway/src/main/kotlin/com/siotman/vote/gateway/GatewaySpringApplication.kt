package com.siotman.vote.gateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GatewaySpringApplication

fun main(args: Array<String>) {
    runApplication<GatewaySpringApplication>(*args)
}
