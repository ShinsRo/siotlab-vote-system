package com.siotman.vote.campaign.application

import com.siotman.vote.campaign.infra.InMemoryCampaignRepository
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class CampaignReadServiceTest {
    private val campaignRepository = InMemoryCampaignRepository()
    private val campaignReadService = CampaignReadService(campaignRepository)

    @Test
    fun `존재하지 않는 캠페인 조회 시 예외가 발생한다`() {
        assertThatThrownBy { campaignReadService.getById(999L) }
            .isInstanceOf(CampaignNotFoundException::class.java)
    }
}
