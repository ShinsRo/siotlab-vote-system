package com.siotman.vote.policy.infra

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface VotePolicyR2dbcRepository : ReactiveCrudRepository<VotePolicyR2dbcEntity, Long> {
    fun findAllByOrderByCreatedAtDesc(): Flux<VotePolicyR2dbcEntity>
}
