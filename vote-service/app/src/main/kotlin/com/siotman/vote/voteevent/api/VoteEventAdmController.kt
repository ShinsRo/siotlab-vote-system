package com.siotman.vote.voteevent.api

import com.siotman.vote.voteevent.application.VoteEventAdmService
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
@RequestMapping("/api/v1/adm/vote-events")
class VoteEventAdmController(
    private val voteEventAdmService: VoteEventAdmService,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody request: CreateVoteEventRequest): Mono<VoteEventResponse> {
        return voteEventAdmService.create(request.toCommand())
            .map(VoteEventResponse::from)
    }

    @PatchMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody request: UpdateVoteEventRequest,
    ): Mono<VoteEventResponse> {
        return voteEventAdmService.update(request.toCommand(id))
            .map(VoteEventResponse::from)
    }

    @PatchMapping("/{id}/activate")
    fun activate(@PathVariable id: Long): Mono<VoteEventResponse> {
        return voteEventAdmService.activate(id)
            .map(VoteEventResponse::from)
    }

    @PatchMapping("/{id}/close")
    fun close(@PathVariable id: Long): Mono<VoteEventResponse> {
        return voteEventAdmService.close(id)
            .map(VoteEventResponse::from)
    }
}
