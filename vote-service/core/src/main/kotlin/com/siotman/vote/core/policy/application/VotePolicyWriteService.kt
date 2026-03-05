package com.siotman.vote.core.policy.application

import com.siotman.vote.core.policy.domain.VotePolicy
import com.siotman.vote.core.policy.domain.spec.VotePolicySpecSerde
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Service
class VotePolicyWriteService(
    private val votePolicyRepository: VotePolicyRepository,
) {
    fun create(command: CreateVotePolicyCommand): Mono<VotePolicy> {
        val now = LocalDateTime.now()
        val votePolicy = VotePolicy(
            id = null,
            name = command.name,
            spec = VotePolicySpecSerde.deserialize(command.type, command.params),
            createdAt = now,
            updatedAt = now,
        )
        return votePolicyRepository.save(votePolicy)
    }

    fun update(command: UpdateVotePolicyCommand): Mono<VotePolicy> {
        return votePolicyRepository.findByIdOrThrow(command.id)
            .map {
                it.update(
                    name = command.name,
                    spec = VotePolicySpecSerde.deserialize(command.type, command.params),
                    updatedAt = LocalDateTime.now(),
                )
            }
            .flatMap { votePolicyRepository.save(it) }
    }

    fun delete(id: Long): Mono<Void> {
        return votePolicyRepository.findByIdOrThrow(id)
            .flatMap { votePolicyRepository.deleteById(id) }
    }

    private fun VotePolicyRepository.findByIdOrThrow(id: Long): Mono<VotePolicy> {
        return findById(id)
            .switchIfEmpty(Mono.error(VotePolicyNotFoundException(id)))
    }
}
