package com.siotman.vote.core.voterecord.infra

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface VoteEventCandidateR2dbcRepository : ReactiveCrudRepository<VoteEventCandidateR2dbcEntity, Long> {
    fun findAllByEventId(eventId: Long): Flux<VoteEventCandidateR2dbcEntity>
}
