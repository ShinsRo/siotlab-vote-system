package com.siotman.vote.api.policy

import com.siotman.vote.core.policy.domain.VotePolicy
import java.time.LocalDateTime

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
