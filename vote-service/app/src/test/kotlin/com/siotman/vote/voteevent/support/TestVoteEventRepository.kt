package com.siotman.vote.voteevent.support

import com.siotman.vote.voteevent.application.VoteEventRepository
import com.siotman.vote.voteevent.domain.VoteEvent
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

class TestVoteEventRepository : VoteEventRepository {
    private val sequence = AtomicLong(0)
    private val voteEvents = ConcurrentHashMap<Long, VoteEvent>()

    override fun save(voteEvent: VoteEvent): Mono<VoteEvent> {
        val persisted = if (voteEvent.id == null) {
            VoteEvent(
                id = sequence.incrementAndGet(),
                campaignId = voteEvent.campaignId,
                policyId = voteEvent.policyId,
                name = voteEvent.name,
                description = voteEvent.description,
                startAt = voteEvent.startAt,
                endAt = voteEvent.endAt,
                status = voteEvent.status,
                createdBy = voteEvent.createdBy,
                createdAt = voteEvent.createdAt,
                updatedAt = voteEvent.updatedAt,
            )
        } else {
            voteEvent
        }
        voteEvents[requireNotNull(persisted.id)] = persisted
        return Mono.just(persisted)
    }

    override fun findById(id: Long): Mono<VoteEvent> {
        val voteEvent = voteEvents[id] ?: return Mono.empty()
        return Mono.just(voteEvent)
    }

    override fun findAll(): Flux<VoteEvent> {
        return Flux.fromIterable(voteEvents.values.sortedByDescending { it.createdAt })
    }
}
