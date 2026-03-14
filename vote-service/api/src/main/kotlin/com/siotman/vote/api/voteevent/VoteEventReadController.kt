package com.siotman.vote.api.voteevent

import com.siotman.vote.core.voteevent.application.VoteEventReadService
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
@RequestMapping("/api/v1/vote-events")
@Tag(name = "API Vote Event", description = "투표 이벤트 조회 API")
class VoteEventReadController(
    private val voteEventReadService: VoteEventReadService,
) {
    @GetMapping("/{id}")
    @Operation(summary = "투표 이벤트 단건 조회")
    fun get(@PathVariable id: Long): Mono<ApiResponse<VoteEventResponse>> {
        return voteEventReadService.getById(id)
            .map(VoteEventResponse::from)
            .toApiResponse()
    }

    @GetMapping
    @Operation(summary = "투표 이벤트 목록 조회")
    fun list(): Mono<ApiResponse<List<VoteEventResponse>>> {
        return voteEventReadService.getAll()
            .map(VoteEventResponse::from)
            .toListApiResponse()
    }
}
