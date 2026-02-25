package com.siotman.vote.campaign.api

import com.siotman.vote.campaign.application.CampaignReadService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/campaigns")
class CampaignReadController(
    private val campaignReadService: CampaignReadService,
) {
    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): Mono<CampaignResponse> {
        return campaignReadService.getById(id)
            .map(CampaignResponse::from)
    }

    // TODO: 조건 기반 조회로 확장
    @GetMapping
    fun list(): Flux<CampaignResponse> {
        return campaignReadService.getAll()
            .map(CampaignResponse::from)
    }
}
