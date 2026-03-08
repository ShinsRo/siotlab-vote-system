package com.siotman.vote.core.voterecord.infra

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("vote_event_candidate")
data class VoteEventCandidateR2dbcEntity(
    @Id
    @Column("candidate_id")
    val candidateId: Long,
    @Column("event_id")
    val eventId: Long,
    @Column("display_order")
    val displayOrder: Int,
    @Column("visibility")
    val visibility: String,
    @Column("created_at")
    val createdAt: LocalDateTime,
    @Column("updated_at")
    val updatedAt: LocalDateTime,
)
