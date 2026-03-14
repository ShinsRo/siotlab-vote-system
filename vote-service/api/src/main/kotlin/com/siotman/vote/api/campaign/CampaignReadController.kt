package com.siotman.vote.api.campaign

import com.siotman.vote.core.campaign.application.CampaignReadService
import com.siotman.vote.core.common.api.ApiResponse
import com.siotman.vote.core.common.api.toApiResponse
import com.siotman.vote.core.common.api.toListApiResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/campaigns")
@Tag(name = "API Campaign", description = "캠페인 조회 API")
class CampaignReadController(
    private val campaignReadService: CampaignReadService,
) {
    @GetMapping("/{id}")
    @Operation(summary = "캠페인 단건 조회")
    fun get(@PathVariable id: Long): Mono<ApiResponse<CampaignResponse>> {
        return campaignReadService.getById(id)
            .map(CampaignResponse::from)
            .toApiResponse()
    }

    // TODO: 조건 기반 조회로 확장
    @GetMapping
    @Operation(summary = "캠페인 목록 조회")
    fun list(): Mono<ApiResponse<List<CampaignResponse>>> {
        return campaignReadService.getAll()
            .map(CampaignResponse::from)
            .toListApiResponse()
    }
}
