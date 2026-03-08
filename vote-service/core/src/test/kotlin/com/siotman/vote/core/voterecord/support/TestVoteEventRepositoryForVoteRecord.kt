package com.siotman.vote.core.voterecord.support

import com.siotman.vote.core.voteevent.application.VoteEventRepository
import com.siotman.vote.core.voteevent.domain.VoteEvent
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.concurrent.ConcurrentHashMap

class TestVoteEventRepositoryForVoteRecord : VoteEventRepository {
    private val voteEvents = ConcurrentHashMap<Long, VoteEvent>()

    fun put(voteEvent: VoteEvent) {
        voteEvents[requireNotNull(voteEvent.id)] = voteEvent
    }

    override fun save(voteEvent: VoteEvent): Mono<VoteEvent> {
        voteEvents[requireNotNull(voteEvent.id)] = voteEvent
        return Mono.just(voteEvent)
    }

    override fun findById(id: Long): Mono<VoteEvent> {
        val voteEvent = voteEvents[id] ?: return Mono.empty()
        return Mono.just(voteEvent)
    }

    override fun findAll(): Flux<VoteEvent> {
        return Flux.fromIterable(voteEvents.values)
    }
}
