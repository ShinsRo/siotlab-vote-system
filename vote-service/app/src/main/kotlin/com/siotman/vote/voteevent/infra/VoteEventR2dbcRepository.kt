package com.siotman.vote.voteevent.infra

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface VoteEventR2dbcRepository : ReactiveCrudRepository<VoteEventR2dbcEntity, Long> {
    fun findAllByOrderByCreatedAtDesc(): Flux<VoteEventR2dbcEntity>
}
