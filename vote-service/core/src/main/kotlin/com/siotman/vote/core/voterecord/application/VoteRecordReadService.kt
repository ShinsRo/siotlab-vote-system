package com.siotman.vote.core.voterecord.application

import com.siotman.vote.core.voterecord.domain.VoteRecord
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class VoteRecordReadService(
    private val voteRecordRepository: VoteRecordRepository,
) {
    fun getById(id: Long): Mono<VoteRecord> {
        return voteRecordRepository.findById(id)
            .switchIfEmpty(Mono.error(VoteRecordNotFoundException(id)))
    }

    fun getByEventId(eventId: Long): Flux<VoteRecord> {
        require(eventId > 0) { "eventId는 0보다 커야 합니다." }
        return voteRecordRepository.findAllByEventId(eventId)
    }

    fun getByEventIdAndUserId(eventId: Long, userId: String): Flux<VoteRecord> {
        require(eventId > 0) { "eventId는 0보다 커야 합니다." }
        require(userId.isNotBlank()) { "userId는 비어 있을 수 없습니다." }
        return voteRecordRepository.findAllByEventIdAndUserId(eventId, userId)
    }
}
