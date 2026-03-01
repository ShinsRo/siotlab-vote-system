package com.siotman.vote.policy.support

import com.siotman.vote.policy.application.VotePolicyRepository
import com.siotman.vote.policy.domain.VotePolicy
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

class TestVotePolicyRepository : VotePolicyRepository {
    private val sequence = AtomicLong(0)
    private val policies = ConcurrentHashMap<Long, VotePolicy>()

    override fun save(votePolicy: VotePolicy): Mono<VotePolicy> {
        val persisted = if (votePolicy.id == null) {
            VotePolicy(
                id = sequence.incrementAndGet(),
                name = votePolicy.name,
                type = votePolicy.type,
                params = votePolicy.params,
                createdAt = votePolicy.createdAt,
                updatedAt = votePolicy.updatedAt,
            )
        } else {
            votePolicy
        }
        policies[requireNotNull(persisted.id)] = persisted
        return Mono.just(persisted)
    }

    override fun findById(id: Long): Mono<VotePolicy> {
        val policy = policies[id] ?: return Mono.empty()
        return Mono.just(policy)
    }

    override fun findAll(): Flux<VotePolicy> {
        return Flux.fromIterable(policies.values.sortedByDescending { it.createdAt })
    }

    override fun deleteById(id: Long): Mono<Void> {
        policies.remove(id)
        return Mono.empty()
    }
}
