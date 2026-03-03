package com.siotman.vote.core.voteevent.application

import com.siotman.vote.core.voteevent.domain.VoteEvent
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface VoteEventRepository {
    fun save(voteEvent: VoteEvent): Mono<VoteEvent>
    fun findById(id: Long): Mono<VoteEvent>
    fun findAll(): Flux<VoteEvent>
}
