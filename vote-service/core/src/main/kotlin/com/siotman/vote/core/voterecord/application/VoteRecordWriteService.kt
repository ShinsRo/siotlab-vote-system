package com.siotman.vote.core.voterecord.application

import com.siotman.vote.core.voterecord.domain.VoteRecord
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.time.LocalDateTime

@Service
class VoteRecordWriteService(
    private val voteRecordRepository: VoteRecordRepository,
) {
    fun create(command: CreateVoteRecordCommand, refAt: LocalDateTime = LocalDateTime.now()): Flux<VoteRecord> {
        require(command.eventId > 0) { "eventId는 0보다 커야 합니다." }
        require(command.userId.isNotBlank()) { "userId는 비어 있을 수 없습니다." }

        val candidateIds = command.candidateIds.distinct()
        require(candidateIds.isNotEmpty()) { "candidateIds는 비어 있을 수 없습니다." }
        require(candidateIds.all { it > 0 }) { "candidateIds는 모두 0보다 커야 합니다." }

        val voteRecords = candidateIds.map { candidateId ->
            VoteRecord(
                id = null,
                eventId = command.eventId,
                candidateId = candidateId,
                userId = command.userId,
                votedAt = refAt,
            )
        }
        return voteRecordRepository.saveAll(voteRecords)
    }
}
