package com.siotman.vote.adm.voteevent

import com.siotman.vote.core.voteevent.application.CreateVoteEventCommand
import com.siotman.vote.core.voteevent.application.UpdateVoteEventCommand
import com.siotman.vote.core.voteevent.domain.VoteEvent
import com.siotman.vote.core.voteevent.domain.VoteEventStatus
import java.time.LocalDateTime

data class CreateVoteEventRequest(
    val campaignId: Long?,
    val policyId: Long,
    val name: String,
    val description: String?,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
    val createdBy: String,
) {
    fun toCommand(): CreateVoteEventCommand {
        return CreateVoteEventCommand(
            campaignId = campaignId,
            policyId = policyId,
            name = name,
            description = description,
            startAt = startAt,
            endAt = endAt,
            createdBy = createdBy,
        )
    }
}

data class UpdateVoteEventRequest(
    val name: String,
    val description: String?,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
) {
    fun toCommand(id: Long): UpdateVoteEventCommand {
        return UpdateVoteEventCommand(
            id = id,
            name = name,
            description = description,
            startAt = startAt,
            endAt = endAt,
        )
    }
}

data class VoteEventResponse(
    val id: Long,
    val campaignId: Long?,
    val policyId: Long,
    val name: String,
    val description: String?,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
    val status: VoteEventStatus,
    val createdBy: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    companion object {
        fun from(voteEvent: VoteEvent): VoteEventResponse {
            return VoteEventResponse(
                id = requireNotNull(voteEvent.id) { "투표 이벤트 ID는 null일 수 없습니다." },
                campaignId = voteEvent.campaignId,
                policyId = voteEvent.policyId,
                name = voteEvent.name,
                description = voteEvent.description,
                startAt = voteEvent.startAt,
                endAt = voteEvent.endAt,
                status = voteEvent.status,
                createdBy = voteEvent.createdBy,
                createdAt = voteEvent.createdAt,
                updatedAt = voteEvent.updatedAt,
            )
        }
    }
}

data class VoteEventErrorResponse(
    val message: String,
)
