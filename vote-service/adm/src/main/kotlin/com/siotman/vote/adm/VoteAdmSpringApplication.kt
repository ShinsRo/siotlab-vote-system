package com.siotman.vote.adm

import com.siotman.vote.core.CORE_BASE_PACKAGE
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@SpringBootApplication(scanBasePackages = [CORE_BASE_PACKAGE, "com.siotman.vote.adm"])
@EnableR2dbcRepositories(basePackages = [CORE_BASE_PACKAGE])
class VoteAdmSpringApplication

fun main(args: Array<String>) {
    runApplication<VoteAdmSpringApplication>(*args)
}
