package com.siotman.vote.campaign.domain

import java.time.LocalDateTime

data class Campaign(
    val id: Long?,
    val name: String,
    val description: String?,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
    val status: CampaignStatus,
    val createdBy: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    init {
        require(name.isNotBlank()) { "캠페인 이름은 비어 있을 수 없습니다." }
        require(createdBy.isNotBlank()) { "생성자는 비어 있을 수 없습니다." }
        require(!endAt.isBefore(startAt)) { "캠페인 기간이 올바르지 않습니다. 종료일은 시작일 이후여야 합니다." }
    }

    fun activate(activatedAt: LocalDateTime): Campaign {
        require(status == CampaignStatus.DRAFT) { "캠페인은 DRAFT 상태에서만 활성화할 수 있습니다." }
        return copy(status = CampaignStatus.ACTIVE, updatedAt = activatedAt)
    }

    fun close(closedAt: LocalDateTime): Campaign {
        require(status != CampaignStatus.ENDED) { "이미 종료된 캠페인입니다." }
        return copy(status = CampaignStatus.ENDED, updatedAt = closedAt)
    }
}
