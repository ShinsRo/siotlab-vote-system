package com.siotman.vote.voteevent.application

import com.siotman.vote.voteevent.domain.VoteEvent
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class VoteEventReadService(
    private val voteEventRepository: VoteEventRepository,
) {
    fun getById(id: Long): Mono<VoteEvent> {
        return voteEventRepository.findById(id)
            .switchIfEmpty(Mono.error(VoteEventNotFoundException(id)))
    }

    fun getAll(): Flux<VoteEvent> {
        return voteEventRepository.findAll()
    }
}
