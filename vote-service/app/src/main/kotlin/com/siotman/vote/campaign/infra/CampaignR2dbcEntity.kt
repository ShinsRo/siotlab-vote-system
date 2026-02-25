package com.siotman.vote.campaign.infra

import com.siotman.vote.campaign.domain.Campaign
import com.siotman.vote.campaign.domain.CampaignStatus
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("vote_campaign")
class CampaignR2dbcEntity(
    @Id
    val id: Long? = null,
    @Column("name")
    val name: String,
    @Column("description")
    val description: String?,
    @Column("start_at")
    val startAt: LocalDateTime,
    @Column("end_at")
    val endAt: LocalDateTime,
    @Column("status")
    val status: CampaignStatus,
    @Column("created_by")
    val createdBy: String,
    @Column("created_at")
    val createdAt: LocalDateTime,
    @Column("updated_at")
    val updatedAt: LocalDateTime,
) {
    fun toDomain(): Campaign {
        return Campaign(
            id = id,
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
        fun from(domain: Campaign): CampaignR2dbcEntity {
            return CampaignR2dbcEntity(
                id = domain.id,
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
