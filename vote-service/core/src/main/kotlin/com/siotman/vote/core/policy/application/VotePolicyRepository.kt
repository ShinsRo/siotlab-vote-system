package com.siotman.vote.core.policy.application

import com.siotman.vote.core.policy.domain.VotePolicy
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface VotePolicyRepository {
    fun save(votePolicy: VotePolicy): Mono<VotePolicy>
    fun findById(id: Long): Mono<VotePolicy>
    fun findAll(): Flux<VotePolicy>
    fun deleteById(id: Long): Mono<Void>
}
