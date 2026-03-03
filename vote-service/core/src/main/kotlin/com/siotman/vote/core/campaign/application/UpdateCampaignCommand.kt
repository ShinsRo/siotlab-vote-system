package com.siotman.vote.core.campaign.application

import java.time.LocalDateTime

data class UpdateCampaignCommand(
    val id: Long,
    val name: String,
    val description: String?,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
)
