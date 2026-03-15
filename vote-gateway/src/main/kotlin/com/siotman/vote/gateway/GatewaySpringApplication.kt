package com.siotman.vote.gateway

import com.siotman.vote.gateway.auth.config.IdentityTranslationProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan(basePackageClasses = [IdentityTranslationProperties::class])
class GatewaySpringApplication

fun main(args: Array<String>) {
    runApplication<GatewaySpringApplication>(*args)
}
