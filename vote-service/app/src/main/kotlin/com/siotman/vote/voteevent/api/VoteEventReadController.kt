package com.siotman.vote.voteevent.api

import com.siotman.vote.voteevent.application.VoteEventReadService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/vote-events")
class VoteEventReadController(
    private val voteEventReadService: VoteEventReadService,
) {
    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): Mono<VoteEventResponse> {
        return voteEventReadService.getById(id)
            .map(VoteEventResponse::from)
    }

    @GetMapping
    fun list(): Flux<VoteEventResponse> {
        return voteEventReadService.getAll()
            .map(VoteEventResponse::from)
    }
}
