package com.siotman.vote.core.campaign.infra

import com.siotman.vote.core.campaign.application.CampaignRepository
import com.siotman.vote.core.campaign.domain.Campaign
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class CampaignR2dbcRepositoryAdapter(
    private val campaignR2dbcRepository: CampaignR2dbcRepository,
) : CampaignRepository {
    override fun save(campaign: Campaign): Mono<Campaign> {
        return campaignR2dbcRepository.save(CampaignR2dbcEntity.from(campaign))
            .map { it.toDomain() }
    }

    override fun findById(id: Long): Mono<Campaign> {
        return campaignR2dbcRepository.findById(id)
            .map { it.toDomain() }
    }

    override fun findAll(): Flux<Campaign> {
        return campaignR2dbcRepository.findAllByOrderByCreatedAtDesc()
            .map { it.toDomain() }
    }
}
