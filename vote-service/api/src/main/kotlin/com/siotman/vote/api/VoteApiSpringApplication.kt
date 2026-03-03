package com.siotman.vote.api

import com.siotman.vote.core.CORE_BASE_PACKAGE
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@SpringBootApplication(scanBasePackages = [CORE_BASE_PACKAGE, "com.siotman.vote.api"])
@EnableR2dbcRepositories(basePackages = [CORE_BASE_PACKAGE])
class VoteApiSpringApplication

fun main(args: Array<String>) {
    runApplication<VoteApiSpringApplication>(*args)
}
