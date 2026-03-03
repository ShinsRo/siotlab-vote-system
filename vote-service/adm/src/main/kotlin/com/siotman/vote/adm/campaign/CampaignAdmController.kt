package com.siotman.vote.adm.campaign

import com.siotman.vote.core.campaign.application.CampaignWriteService
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
    private val campaignWriteService: CampaignWriteService,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody request: CreateCampaignRequest): Mono<CampaignResponse> {
        return campaignWriteService.create(request.toCommand())
            .map(CampaignResponse::from)
    }

    @PatchMapping("/{id}/close")
    fun close(@PathVariable id: Long): Mono<CampaignResponse> {
        return campaignWriteService.close(id)
            .map(CampaignResponse::from)
    }

    @PatchMapping("/{id}/activate")
    fun activate(@PathVariable id: Long): Mono<CampaignResponse> {
        return campaignWriteService.activate(id)
            .map(CampaignResponse::from)
    }

    @PatchMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody request: UpdateCampaignRequest,
    ): Mono<CampaignResponse> {
        return campaignWriteService.update(request.toCommand(id))
            .map(CampaignResponse::from)
    }
}
