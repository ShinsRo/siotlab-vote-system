package com.siotman.vote.campaign.infra

import com.siotman.vote.campaign.application.CampaignRepository
import com.siotman.vote.campaign.domain.Campaign
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

@Repository // 임시 인메모리. TODO: 추후 DB 리포지토리로 교체
class InMemoryCampaignRepository : CampaignRepository {
    private val sequence = AtomicLong(0)
    private val campaigns = ConcurrentHashMap<Long, Campaign>()

    override fun save(campaign: Campaign): Campaign {
        val id = campaign.id ?: sequence.incrementAndGet()
        val persisted = campaign.copy(id = id)
        campaigns[id] = persisted
        return persisted
    }

    override fun findById(id: Long): Campaign? {
        return campaigns[id]
    }

    override fun findAll(): List<Campaign> {
        return campaigns.values
            .sortedByDescending { it.createdAt }
    }
}
