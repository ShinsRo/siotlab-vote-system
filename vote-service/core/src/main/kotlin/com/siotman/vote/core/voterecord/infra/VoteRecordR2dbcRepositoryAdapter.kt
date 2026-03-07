package com.siotman.vote.core.voterecord.infra

import com.siotman.vote.core.voterecord.application.VoteRecordRepository
import com.siotman.vote.core.voterecord.domain.VoteRecord
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class VoteRecordR2dbcRepositoryAdapter(
    private val voteRecordR2dbcRepository: VoteRecordR2dbcRepository,
) : VoteRecordRepository {
    override fun saveAll(voteRecords: List<VoteRecord>): Flux<VoteRecord> {
        return voteRecordR2dbcRepository.saveAll(voteRecords.map(VoteRecordR2dbcEntity::from))
            .map(VoteRecordR2dbcEntity::toDomain)
    }

    override fun findById(id: Long): Mono<VoteRecord> {
        return voteRecordR2dbcRepository.findById(id)
            .map(VoteRecordR2dbcEntity::toDomain)
    }

    override fun findAllByEventId(eventId: Long): Flux<VoteRecord> {
        return voteRecordR2dbcRepository.findAllByEventIdOrderByVotedAtDesc(eventId)
            .map(VoteRecordR2dbcEntity::toDomain)
    }

    override fun findAllByEventIdAndUserId(eventId: Long, userId: String): Flux<VoteRecord> {
        return voteRecordR2dbcRepository.findAllByEventIdAndUserIdOrderByVotedAtDesc(eventId, userId)
            .map(VoteRecordR2dbcEntity::toDomain)
    }
}
