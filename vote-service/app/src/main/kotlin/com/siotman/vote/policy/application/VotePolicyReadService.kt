package com.siotman.vote.policy.application

import com.siotman.vote.policy.domain.VotePolicy
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class VotePolicyReadService(
    private val votePolicyRepository: VotePolicyRepository,
) {
    fun getById(id: Long): Mono<VotePolicy> {
        return votePolicyRepository.findById(id)
            .switchIfEmpty(Mono.error(VotePolicyNotFoundException(id)))
    }

    fun getAll(): Flux<VotePolicy> {
        return votePolicyRepository.findAll()
    }
}
