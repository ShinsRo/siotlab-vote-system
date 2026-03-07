package com.siotman.vote.core.voterecord.infra

import com.siotman.vote.core.voterecord.domain.VoteRecord
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("vote_record")
data class VoteRecordR2dbcEntity(
    @Id
    val id: Long? = null,
    @Column("event_id")
    val eventId: Long,
    @Column("candidate_id")
    val candidateId: Long,
    @Column("user_id")
    val userId: String,
    @Column("voted_at")
    val votedAt: LocalDateTime,
) {
    fun toDomain(): VoteRecord {
        return VoteRecord(
            id = id,
            eventId = eventId,
            candidateId = candidateId,
            userId = userId,
            votedAt = votedAt,
        )
    }

    companion object {
        fun from(domain: VoteRecord): VoteRecordR2dbcEntity {
            return VoteRecordR2dbcEntity(
                id = domain.id,
                eventId = domain.eventId,
                candidateId = domain.candidateId,
                userId = domain.userId,
                votedAt = domain.votedAt,
            )
        }
    }
}
