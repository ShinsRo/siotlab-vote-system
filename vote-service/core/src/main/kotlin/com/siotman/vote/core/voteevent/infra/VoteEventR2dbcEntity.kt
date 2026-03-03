package com.siotman.vote.core.voteevent.infra

import com.siotman.vote.core.voteevent.domain.VoteEvent
import com.siotman.vote.core.voteevent.domain.VoteEventStatus
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("vote_event")
data class VoteEventR2dbcEntity(
    @Id
    val id: Long? = null,
    @Column("campaign_id")
    val campaignId: Long?,
    @Column("policy_id")
    val policyId: Long,
    @Column("name")
    val name: String,
    @Column("description")
    val description: String?,
    @Column("start_at")
    val startAt: LocalDateTime,
    @Column("end_at")
    val endAt: LocalDateTime,
    @Column("status")
    val status: VoteEventStatus,
    @Column("created_by")
    val createdBy: String,
    @Column("created_at")
    val createdAt: LocalDateTime,
    @Column("updated_at")
    val updatedAt: LocalDateTime,
) {
    fun toDomain(): VoteEvent {
        return VoteEvent(
            id = id,
            campaignId = campaignId,
            policyId = policyId,
            name = name,
            description = description,
            startAt = startAt,
            endAt = endAt,
            status = status,
            createdBy = createdBy,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
    }

    companion object {
        fun from(domain: VoteEvent): VoteEventR2dbcEntity {
            return VoteEventR2dbcEntity(
                id = domain.id,
                campaignId = domain.campaignId,
                policyId = domain.policyId,
                name = domain.name,
                description = domain.description,
                startAt = domain.startAt,
                endAt = domain.endAt,
                status = domain.status,
                createdBy = domain.createdBy,
                createdAt = domain.createdAt,
                updatedAt = domain.updatedAt,
            )
        }
    }
}
