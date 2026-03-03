package com.siotman.vote.core.candidate.infra

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface CandidateR2dbcRepository : ReactiveCrudRepository<CandidateR2dbcEntity, Long> {
    fun findAllByOrderByCreatedAtDesc(): Flux<CandidateR2dbcEntity>
}
