package com.siotman.vote.adm.campaign

import com.siotman.vote.core.campaign.application.CreateCampaignCommand
import com.siotman.vote.core.campaign.application.UpdateCampaignCommand
import com.siotman.vote.core.campaign.domain.Campaign
import com.siotman.vote.core.campaign.domain.CampaignStatus
import java.time.LocalDateTime

data class CreateCampaignRequest(
    val name: String,
    val description: String?,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
    val createdBy: String,
) {
    fun toCommand(): CreateCampaignCommand {
        return CreateCampaignCommand(
            name = name,
            description = description,
            startAt = startAt,
            endAt = endAt,
            createdBy = createdBy,
        )
    }
}

data class UpdateCampaignRequest(
    val name: String,
    val description: String?,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
) {
    fun toCommand(id: Long): UpdateCampaignCommand {
        return UpdateCampaignCommand(
            id = id,
            name = name,
            description = description,
            startAt = startAt,
            endAt = endAt,
        )
    }
}

data class CampaignResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
    val status: CampaignStatus,
    val createdBy: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    companion object {
        fun from(campaign: Campaign): CampaignResponse {
            return CampaignResponse(
                id = checkNotNull(campaign.id) { "캠페인 ID는 null일 수 없습니다." },
                name = campaign.name,
                description = campaign.description,
                startAt = campaign.startAt,
                endAt = campaign.endAt,
                status = campaign.status,
                createdBy = campaign.createdBy,
                createdAt = campaign.createdAt,
                updatedAt = campaign.updatedAt,
            )
        }
    }
}

data class ErrorResponse(
    val message: String,
)
