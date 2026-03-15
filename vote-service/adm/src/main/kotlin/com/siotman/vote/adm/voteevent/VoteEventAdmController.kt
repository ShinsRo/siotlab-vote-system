package com.siotman.vote.adm.voteevent

import com.siotman.vote.adm.common.web.PRINCIPAL_ID_HEADER
import com.siotman.vote.adm.common.web.requirePrincipalId
import com.siotman.vote.core.voteevent.application.VoteEventWriteService
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
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/adm/vote-events")
@Tag(name = "ADM Vote Event", description = "관리자 투표 이벤트 API")
class VoteEventAdmController(
    private val voteEventWriteService: VoteEventWriteService,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "투표 이벤트 생성")
    fun create(
        @RequestHeader(name = PRINCIPAL_ID_HEADER, required = false) principalId: String?,
        @RequestBody request: CreateVoteEventRequest,
    ): Mono<ApiResponse<VoteEventResponse>> {
        return voteEventWriteService.create(request.toCommand(createdBy = requirePrincipalId(principalId)))
            .map(VoteEventResponse::from)
            .toApiResponse()
    }

    @PatchMapping("/{id}")
    @Operation(summary = "투표 이벤트 수정")
    fun update(
        @PathVariable id: Long,
        @RequestBody request: UpdateVoteEventRequest,
    ): Mono<ApiResponse<VoteEventResponse>> {
        return voteEventWriteService.update(request.toCommand(id))
            .map(VoteEventResponse::from)
            .toApiResponse()
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "투표 이벤트 활성화")
    fun activate(@PathVariable id: Long): Mono<ApiResponse<VoteEventResponse>> {
        return voteEventWriteService.activate(id)
            .map(VoteEventResponse::from)
            .toApiResponse()
    }

    @PatchMapping("/{id}/close")
    @Operation(summary = "투표 이벤트 종료")
    fun close(@PathVariable id: Long): Mono<ApiResponse<VoteEventResponse>> {
        return voteEventWriteService.close(id)
            .map(VoteEventResponse::from)
            .toApiResponse()
    }
}
