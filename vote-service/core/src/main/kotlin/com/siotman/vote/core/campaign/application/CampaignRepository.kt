package com.siotman.vote.core.campaign.application

import com.siotman.vote.core.campaign.domain.Campaign
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface CampaignRepository {
    fun save(campaign: Campaign): Mono<Campaign>
    fun findById(id: Long): Mono<Campaign>
    fun findAll(): Flux<Campaign>
}
