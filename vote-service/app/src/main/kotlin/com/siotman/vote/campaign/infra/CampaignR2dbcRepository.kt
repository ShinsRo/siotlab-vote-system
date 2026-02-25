package com.siotman.vote.campaign.infra

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface CampaignR2dbcRepository : ReactiveCrudRepository<CampaignR2dbcEntity, Long> {
    fun findAllByOrderByCreatedAtDesc(): Flux<CampaignR2dbcEntity>
}
