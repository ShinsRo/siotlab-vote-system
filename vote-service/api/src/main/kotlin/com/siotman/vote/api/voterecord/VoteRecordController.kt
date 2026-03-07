package com.siotman.vote.api.voterecord

import com.siotman.vote.core.voterecord.application.VoteRecordReadService
import com.siotman.vote.core.voterecord.application.VoteRecordWriteService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/vote-records")
@Tag(name = "API Vote Record", description = "투표 기록 API")
class VoteRecordController(
    private val voteRecordWriteService: VoteRecordWriteService,
    private val voteRecordReadService: VoteRecordReadService,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "투표 기록 생성")
    fun create(@RequestBody request: CreateVoteRecordRequest): Flux<VoteRecordResponse> {
        return voteRecordWriteService.create(request.toCommand())
            .map(VoteRecordResponse::from)
    }

    @GetMapping("/{id}")
    @Operation(summary = "투표 기록 단건 조회")
    fun get(@PathVariable id: Long): Mono<VoteRecordResponse> {
        return voteRecordReadService.getById(id)
            .map(VoteRecordResponse::from)
    }

    @GetMapping
    @Operation(summary = "투표 기록 목록 조회")
    fun list(
        @RequestParam eventId: Long,
        @RequestParam(required = false) userId: String?, // TODO: JWT 에서 subject 추출
    ): Flux<VoteRecordResponse> {
        val records = if (userId.isNullOrBlank()) {
            voteRecordReadService.getByEventId(eventId)
        } else {
            voteRecordReadService.getByEventIdAndUserId(eventId, userId)
        }
        return records.map(VoteRecordResponse::from)
    }
}
