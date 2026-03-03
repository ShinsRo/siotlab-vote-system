package com.siotman.vote.core.campaign.support

import com.siotman.vote.core.campaign.application.CampaignRepository
import com.siotman.vote.core.campaign.domain.Campaign
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

class TestCampaignRepository : CampaignRepository {
    private val sequence = AtomicLong(0)
    private val campaigns = ConcurrentHashMap<Long, Campaign>()

    override fun save(campaign: Campaign): Mono<Campaign> {
        val persisted = if (campaign.id == null) {
            campaign.withId(sequence.incrementAndGet())
        } else {
            campaign
        }
        campaigns[requireNotNull(persisted.id)] = persisted
        return Mono.just(persisted)
    }

    override fun findById(id: Long): Mono<Campaign> {
        val campaign = campaigns[id] ?: return Mono.empty()
        return Mono.just(campaign)
    }

    override fun findAll(): Flux<Campaign> {
        return Flux.fromIterable(campaigns.values.sortedByDescending { it.createdAt })
    }
}
