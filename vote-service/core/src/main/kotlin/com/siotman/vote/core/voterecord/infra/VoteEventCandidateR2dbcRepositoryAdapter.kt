package com.siotman.vote.core.voterecord.infra

import com.siotman.vote.core.voterecord.application.VoteEventCandidateRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
class VoteEventCandidateR2dbcRepositoryAdapter(
    private val voteEventCandidateR2dbcRepository: VoteEventCandidateR2dbcRepository,
) : VoteEventCandidateRepository {
    override fun findCandidateIdsByEventId(eventId: Long): Flux<Long> {
        return voteEventCandidateR2dbcRepository.findAllByEventId(eventId)
            .map(VoteEventCandidateR2dbcEntity::candidateId)
    }
}
