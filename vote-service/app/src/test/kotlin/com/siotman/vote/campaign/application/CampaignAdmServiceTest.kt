package com.siotman.vote.campaign.application

import com.siotman.vote.campaign.infra.InMemoryCampaignRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class CampaignAdmServiceTest {
    private val campaignRepository = InMemoryCampaignRepository()
    private val campaignAdmService = CampaignAdmService(campaignRepository)

    @Test
    fun `관리자가 캠페인을 생성하면 DRAFT 상태로 저장된다`() {
        val command = CreateCampaignCommand(
            name = "3월 인기 영화 투표",
            description = "월간 캠페인",
            startAt = LocalDateTime.of(2026, 3, 1, 0, 0),
            endAt = LocalDateTime.of(2026, 3, 31, 23, 59),
            createdBy = "admin",
        )

        val created = campaignAdmService.create(command)

        assertThat(created.id).isNotNull
        assertThat(created.status.name).isEqualTo("DRAFT")
    }

    @Test
    fun `관리자가 캠페인을 활성화하면 ACTIVE 상태가 된다`() {
        val created = campaignAdmService.create(
            CreateCampaignCommand(
                name = "4월 인기 영화 투표",
                description = "월간 캠페인",
                startAt = LocalDateTime.of(2026, 4, 1, 0, 0),
                endAt = LocalDateTime.of(2026, 4, 30, 23, 59),
                createdBy = "admin",
            ),
        )

        val activated = campaignAdmService.activate(requireNotNull(created.id))

        assertThat(activated.status.name).isEqualTo("ACTIVE")
    }
}
