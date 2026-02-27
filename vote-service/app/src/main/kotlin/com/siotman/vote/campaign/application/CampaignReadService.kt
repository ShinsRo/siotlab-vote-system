package com.siotman.vote.campaign.application

import com.siotman.vote.campaign.domain.Campaign
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class CampaignReadService(
    private val campaignRepository: CampaignRepository,
) {
    fun getById(id: Long): Mono<Campaign> {
        return campaignRepository.findById(id)
            .switchIfEmpty(Mono.error(CampaignNotFoundException(id)))
    }

    fun getAll(): Flux<Campaign> {
        return campaignRepository.findAll()
    }
}
