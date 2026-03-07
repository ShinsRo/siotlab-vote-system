package com.siotman.vote.api.voterecord

import com.siotman.vote.core.voterecord.application.CreateVoteRecordCommand
import com.siotman.vote.core.voterecord.domain.VoteRecord
import java.time.LocalDateTime

data class CreateVoteRecordRequest(
    val eventId: Long,
    val candidateIds: List<Long>,
    val userId: String,
) {
    fun toCommand(): CreateVoteRecordCommand {
        return CreateVoteRecordCommand(
            eventId = eventId,
            candidateIds = candidateIds,
            userId = userId,
        )
    }
}

data class VoteRecordResponse(
    val id: Long,
    val eventId: Long,
    val candidateId: Long,
    val userId: String,
    val votedAt: LocalDateTime,
) {
    companion object {
        fun from(voteRecord: VoteRecord): VoteRecordResponse {
            return VoteRecordResponse(
                id = requireNotNull(voteRecord.id) { "투표 기록 ID는 null일 수 없습니다." },
                eventId = voteRecord.eventId,
                candidateId = voteRecord.candidateId,
                userId = voteRecord.userId,
                votedAt = voteRecord.votedAt,
            )
        }
    }
}

data class VoteRecordErrorResponse(
    val message: String,
)
