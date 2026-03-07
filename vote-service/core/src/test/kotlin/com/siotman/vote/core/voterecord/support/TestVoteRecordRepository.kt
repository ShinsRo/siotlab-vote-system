package com.siotman.vote.core.voterecord.support

import com.siotman.vote.core.voterecord.application.VoteRecordRepository
import com.siotman.vote.core.voterecord.domain.VoteRecord
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

class TestVoteRecordRepository : VoteRecordRepository {
    private val sequence = AtomicLong(0)
    private val storedVoteRecords = ConcurrentHashMap<Long, VoteRecord>()

    override fun saveAll(voteRecords: List<VoteRecord>): Flux<VoteRecord> {
        val persisted = voteRecords.map { voteRecord ->
            if (voteRecord.id == null) {
                VoteRecord(
                    id = sequence.incrementAndGet(),
                    eventId = voteRecord.eventId,
                    candidateId = voteRecord.candidateId,
                    userId = voteRecord.userId,
                    votedAt = voteRecord.votedAt,
                )
            } else {
                voteRecord
            }
        }
        persisted.forEach { storedVoteRecords[requireNotNull(it.id)] = it }
        return Flux.fromIterable(persisted)
    }

    override fun findById(id: Long): Mono<VoteRecord> {
        val voteRecord = storedVoteRecords[id] ?: return Mono.empty()
        return Mono.just(voteRecord)
    }

    override fun findAllByEventId(eventId: Long): Flux<VoteRecord> {
        return Flux.fromIterable(
            storedVoteRecords.values
                .filter { it.eventId == eventId }
                .sortedByDescending { it.votedAt },
        )
    }

    override fun findAllByEventIdAndUserId(eventId: Long, userId: String): Flux<VoteRecord> {
        return Flux.fromIterable(
            storedVoteRecords.values
                .filter { it.eventId == eventId && it.userId == userId }
                .sortedByDescending { it.votedAt },
        )
    }
}
