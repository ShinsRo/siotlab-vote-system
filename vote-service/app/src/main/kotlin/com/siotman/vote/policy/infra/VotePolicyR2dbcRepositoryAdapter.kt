package com.siotman.vote.policy.infra

import com.siotman.vote.policy.application.VotePolicyRepository
import com.siotman.vote.policy.domain.VotePolicy
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class VotePolicyR2dbcRepositoryAdapter(
    private val votePolicyR2dbcRepository: VotePolicyR2dbcRepository,
) : VotePolicyRepository {
    override fun save(votePolicy: VotePolicy): Mono<VotePolicy> {
        return votePolicyR2dbcRepository.save(VotePolicyR2dbcEntity.from(votePolicy))
            .map { it.toDomain() }
    }

    override fun findById(id: Long): Mono<VotePolicy> {
        return votePolicyR2dbcRepository.findById(id)
            .map { it.toDomain() }
    }

    override fun findAll(): Flux<VotePolicy> {
        return votePolicyR2dbcRepository.findAllByOrderByCreatedAtDesc()
            .map { it.toDomain() }
    }

    override fun deleteById(id: Long): Mono<Void> {
        return votePolicyR2dbcRepository.deleteById(id)
    }
}
