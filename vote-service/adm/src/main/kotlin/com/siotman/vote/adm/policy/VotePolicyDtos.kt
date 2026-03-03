package com.siotman.vote.adm.policy

import com.siotman.vote.core.policy.application.CreateVotePolicyCommand
import com.siotman.vote.core.policy.application.UpdateVotePolicyCommand
import com.siotman.vote.core.policy.domain.VotePolicy
import java.time.LocalDateTime

data class CreateVotePolicyRequest(
    val name: String,
    val type: String,
    val params: String,
) {
    fun toCommand(): CreateVotePolicyCommand {
        return CreateVotePolicyCommand(
            name = name,
            type = type,
            params = params,
        )
    }
}

data class UpdateVotePolicyRequest(
    val name: String,
    val type: String,
    val params: String,
) {
    fun toCommand(id: Long): UpdateVotePolicyCommand {
        return UpdateVotePolicyCommand(
            id = id,
            name = name,
            type = type,
            params = params,
        )
    }
}

data class VotePolicyResponse(
    val id: Long,
    val name: String,
    val type: String,
    val params: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    companion object {
        fun from(votePolicy: VotePolicy): VotePolicyResponse {
            return VotePolicyResponse(
                id = requireNotNull(votePolicy.id) { "투표 정책 ID는 null일 수 없습니다." },
                name = votePolicy.name,
                type = votePolicy.type,
                params = votePolicy.params,
                createdAt = votePolicy.createdAt,
                updatedAt = votePolicy.updatedAt,
            )
        }
    }
}

data class VotePolicyErrorResponse(
    val message: String,
)
