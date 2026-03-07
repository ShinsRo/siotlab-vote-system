package com.siotman.vote.core.voterecord.application

import com.siotman.vote.core.voterecord.domain.VoteRecord
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface VoteRecordRepository {
    fun saveAll(voteRecords: List<VoteRecord>): Flux<VoteRecord>
    fun findById(id: Long): Mono<VoteRecord>
    fun findAllByEventId(eventId: Long): Flux<VoteRecord>
    fun findAllByEventIdAndUserId(eventId: Long, userId: String): Flux<VoteRecord>
}
