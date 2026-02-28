package com.siotman.vote.voteevent.infra

import com.siotman.vote.voteevent.application.VoteEventRepository
import com.siotman.vote.voteevent.domain.VoteEvent
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class VoteEventR2dbcRepositoryAdapter(
    private val voteEventR2dbcRepository: VoteEventR2dbcRepository,
) : VoteEventRepository {
    override fun save(voteEvent: VoteEvent): Mono<VoteEvent> {
        return voteEventR2dbcRepository.save(VoteEventR2dbcEntity.from(voteEvent))
            .map { it.toDomain() }
    }

    override fun findById(id: Long): Mono<VoteEvent> {
        return voteEventR2dbcRepository.findById(id)
            .map { it.toDomain() }
    }

    override fun findAll(): Flux<VoteEvent> {
        return voteEventR2dbcRepository.findAllByOrderByCreatedAtDesc()
            .map { it.toDomain() }
    }
}
