package com.siotman.vote.campaign.application

import com.siotman.vote.campaign.domain.Campaign
import org.springframework.stereotype.Service

@Service
class CampaignReadService(
    private val campaignRepository: CampaignRepository,
) {
    fun getById(id: Long): Campaign {
        return campaignRepository.findById(id) ?: throw CampaignNotFoundException(id)
    }

    fun getAll(): List<Campaign> {
        return campaignRepository.findAll()
    }
}
