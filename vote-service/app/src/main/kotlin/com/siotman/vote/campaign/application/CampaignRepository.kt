package com.siotman.vote.campaign.application

import com.siotman.vote.campaign.domain.Campaign

interface CampaignRepository {
    fun save(campaign: Campaign): Campaign
    fun findById(id: Long): Campaign?
    fun findAll(): List<Campaign>
}
