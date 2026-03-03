package com.siotman.vote.core.campaign.domain

import java.time.LocalDateTime

class Campaign(
    val id: Long?,
    name: String,
    description: String?,
    startAt: LocalDateTime,
    endAt: LocalDateTime,
    status: CampaignStatus,
    val createdBy: String,
    val createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
) {
    var name: String = name
        private set

    var description: String? = description
        private set

    var startAt: LocalDateTime = startAt
        private set

    var endAt: LocalDateTime = endAt
        private set

    var status: CampaignStatus = status
        private set

    var updatedAt: LocalDateTime = updatedAt
        private set

    init {
        require(name.isNotBlank()) { "캠페인 이름은 비어 있을 수 없습니다." }
        require(createdBy.isNotBlank()) { "생성자는 비어 있을 수 없습니다." }
        require(!endAt.isBefore(startAt)) { "캠페인 기간이 올바르지 않습니다. 종료일은 시작일 이후여야 합니다." }
    }

    fun activate(activatedAt: LocalDateTime): Campaign {
        require(status == CampaignStatus.DRAFT) { "캠페인은 DRAFT 상태에서만 활성화할 수 있습니다." }
        status = CampaignStatus.ACTIVE
        updatedAt = activatedAt
        return this
    }

    fun update(
        name: String,
        description: String?,
        startAt: LocalDateTime,
        endAt: LocalDateTime,
        updatedAt: LocalDateTime,
    ): Campaign {
        require(status != CampaignStatus.ENDED) { "종료된 캠페인은 수정할 수 없습니다." }
        require(name.isNotBlank()) { "캠페인 이름은 비어 있을 수 없습니다." }
        require(!endAt.isBefore(startAt)) { "캠페인 기간이 올바르지 않습니다. 종료일은 시작일 이후여야 합니다." }

        this.name = name
        this.description = description
        this.startAt = startAt
        this.endAt = endAt
        this.updatedAt = updatedAt
        return this
    }

    fun close(closedAt: LocalDateTime): Campaign {
        require(status != CampaignStatus.ENDED) { "이미 종료된 캠페인입니다." }
        status = CampaignStatus.ENDED
        updatedAt = closedAt
        return this
    }

    fun withId(newId: Long): Campaign {
        return Campaign(
            id = newId,
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
}
