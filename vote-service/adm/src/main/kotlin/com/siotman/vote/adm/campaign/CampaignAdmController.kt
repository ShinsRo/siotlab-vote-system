package com.siotman.vote.adm.campaign

import com.siotman.vote.core.campaign.application.CampaignWriteService
import com.siotman.vote.core.common.api.ApiResponse
import com.siotman.vote.core.common.api.toApiResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
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
@Tag(name = "ADM Campaign", description = "관리자 캠페인 API")
class CampaignAdmController(
    private val campaignWriteService: CampaignWriteService,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "캠페인 생성")
    fun create(@RequestBody request: CreateCampaignRequest): Mono<ApiResponse<CampaignResponse>> {
        return campaignWriteService.create(request.toCommand())
            .map(CampaignResponse::from)
            .toApiResponse()
    }

    @PatchMapping("/{id}/close")
    @Operation(summary = "캠페인 종료")
    fun close(@PathVariable id: Long): Mono<ApiResponse<CampaignResponse>> {
        return campaignWriteService.close(id)
            .map(CampaignResponse::from)
            .toApiResponse()
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "캠페인 활성화")
    fun activate(@PathVariable id: Long): Mono<ApiResponse<CampaignResponse>> {
        return campaignWriteService.activate(id)
            .map(CampaignResponse::from)
            .toApiResponse()
    }

    @PatchMapping("/{id}")
    @Operation(summary = "캠페인 수정")
    fun update(
        @PathVariable id: Long,
        @RequestBody request: UpdateCampaignRequest,
    ): Mono<ApiResponse<CampaignResponse>> {
        return campaignWriteService.update(request.toCommand(id))
            .map(CampaignResponse::from)
            .toApiResponse()
    }
}
