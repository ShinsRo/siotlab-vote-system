package com.siotman.vote.campaign.api

import com.siotman.vote.campaign.application.CampaignAdmService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/adm/campaigns")
class CampaignAdmController(
    private val campaignAdmService: CampaignAdmService,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody request: CreateCampaignRequest): Mono<CampaignResponse> {
        return campaignAdmService.create(request.toCommand())
            .map(CampaignResponse::from)
    }

    @PatchMapping("/{id}/close")
    fun close(@PathVariable id: Long): Mono<CampaignResponse> {
        return campaignAdmService.close(id)
            .map(CampaignResponse::from)
    }

    @PatchMapping("/{id}/activate")
    fun activate(@PathVariable id: Long): Mono<CampaignResponse> {
        return campaignAdmService.activate(id)
            .map(CampaignResponse::from)
    }

    @PatchMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody request: UpdateCampaignRequest,
    ): Mono<CampaignResponse> {
        return campaignAdmService.update(request.toCommand(id))
            .map(CampaignResponse::from)
    }
}
