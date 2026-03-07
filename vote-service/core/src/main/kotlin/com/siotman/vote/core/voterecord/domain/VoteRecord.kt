package com.siotman.vote.core.voterecord.domain

import java.time.LocalDateTime

class VoteRecord(
    val id: Long?,
    val eventId: Long,
    val candidateId: Long,
    val userId: String,
    val votedAt: LocalDateTime,
) {
    init {
        require(eventId > 0) { "eventId는 0보다 커야 합니다." }
        require(candidateId > 0) { "candidateId는 0보다 커야 합니다." }
        require(userId.isNotBlank()) { "userId는 비어 있을 수 없습니다." }
    }
}
