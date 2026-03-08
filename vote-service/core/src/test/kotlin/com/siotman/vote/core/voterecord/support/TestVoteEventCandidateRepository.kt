package com.siotman.vote.core.voterecord.support

import com.siotman.vote.core.voterecord.application.VoteEventCandidateRepository
import reactor.core.publisher.Flux
import java.util.concurrent.ConcurrentHashMap

class TestVoteEventCandidateRepository : VoteEventCandidateRepository {
    private val candidateIdsByEventId = ConcurrentHashMap<Long, Set<Long>>()

    fun save(eventId: Long, candidateIds: Set<Long>) {
        candidateIdsByEventId[eventId] = candidateIds
    }

    override fun findCandidateIdsByEventId(eventId: Long): Flux<Long> {
        return Flux.fromIterable(candidateIdsByEventId[eventId].orEmpty())
    }
}
