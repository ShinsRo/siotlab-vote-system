package com.siotman.vote.campaign.application

import com.siotman.vote.campaign.support.TestCampaignRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class CampaignAdmServiceTest {
    private val campaignRepository = TestCampaignRepository()
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
            .block()!!

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
        ).block()!!

        val activated = campaignAdmService.activate(requireNotNull(created.id))
            .block()!!

        assertThat(activated.status.name).isEqualTo("ACTIVE")
    }

    @Test
    fun `관리자가 캠페인 이름 설명 기간을 수정할 수 있다`() {
        val created = campaignAdmService.create(
            CreateCampaignCommand(
                name = "초기 이름",
                description = "초기 설명",
                startAt = LocalDateTime.of(2026, 5, 1, 0, 0),
                endAt = LocalDateTime.of(2026, 5, 31, 23, 59),
                createdBy = "admin",
            ),
        ).block()!!

        val updated = campaignAdmService.update(
            UpdateCampaignCommand(
                id = requireNotNull(created.id),
                name = "수정 이름",
                description = "수정 설명",
                startAt = LocalDateTime.of(2026, 5, 2, 0, 0),
                endAt = LocalDateTime.of(2026, 6, 1, 23, 59),
            ),
        ).block()!!

        assertThat(updated.name).isEqualTo("수정 이름")
        assertThat(updated.description).isEqualTo("수정 설명")
        assertThat(updated.startAt).isEqualTo(LocalDateTime.of(2026, 5, 2, 0, 0))
        assertThat(updated.endAt).isEqualTo(LocalDateTime.of(2026, 6, 1, 23, 59))
    }
}
