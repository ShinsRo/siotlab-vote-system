package com.siotman.vote

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class VoteSpringApplication

fun main(args: Array<String>) {
    runApplication<VoteSpringApplication>(*args)
}
