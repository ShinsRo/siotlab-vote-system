package com.siotman.vote.core.voterecord.infra

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface VoteRecordR2dbcRepository : ReactiveCrudRepository<VoteRecordR2dbcEntity, Long> {
    fun findAllByEventIdOrderByVotedAtDesc(eventId: Long): Flux<VoteRecordR2dbcEntity>
    fun findAllByEventIdAndUserIdOrderByVotedAtDesc(eventId: Long, userId: String): Flux<VoteRecordR2dbcEntity>
}
