package com.siotman.vote.core.voterecord.support

import com.siotman.vote.core.policy.application.VotePolicyRepository
import com.siotman.vote.core.policy.domain.VotePolicy
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.concurrent.ConcurrentHashMap

class TestVotePolicyRepositoryForVoteRecord : VotePolicyRepository {
    private val votePolicies = ConcurrentHashMap<Long, VotePolicy>()

    fun put(votePolicy: VotePolicy) {
        votePolicies[requireNotNull(votePolicy.id)] = votePolicy
    }

    override fun save(votePolicy: VotePolicy): Mono<VotePolicy> {
        votePolicies[requireNotNull(votePolicy.id)] = votePolicy
        return Mono.just(votePolicy)
    }

    override fun findById(id: Long): Mono<VotePolicy> {
        val votePolicy = votePolicies[id] ?: return Mono.empty()
        return Mono.just(votePolicy)
    }

    override fun findAll(): Flux<VotePolicy> {
        return Flux.fromIterable(votePolicies.values)
    }

    override fun deleteById(id: Long): Mono<Void> {
        votePolicies.remove(id)
        return Mono.empty()
    }
}
