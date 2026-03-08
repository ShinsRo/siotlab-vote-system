package com.siotman.vote.core.voterecord.application

import reactor.core.publisher.Flux

interface VoteEventCandidateRepository {
    fun findCandidateIdsByEventId(eventId: Long): Flux<Long>
}
