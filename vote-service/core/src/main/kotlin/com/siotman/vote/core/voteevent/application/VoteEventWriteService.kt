package com.siotman.vote.core.voteevent.application

import com.siotman.vote.core.voteevent.domain.VoteEvent
import com.siotman.vote.core.voteevent.domain.VoteEventStatus
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Service
class VoteEventWriteService(
    private val voteEventRepository: VoteEventRepository,
) {
    fun create(command: CreateVoteEventCommand, refAt: LocalDateTime = LocalDateTime.now()): Mono<VoteEvent> {
        val voteEvent = VoteEvent(
            id = null,
            campaignId = command.campaignId,
            policyId = command.policyId,
            name = command.name,
            description = command.description,
            startAt = command.startAt,
            endAt = command.endAt,
            status = VoteEventStatus.DRAFT,
            createdBy = command.createdBy,
            createdAt = refAt,
            updatedAt = refAt,
        )
        return voteEventRepository.save(voteEvent)
    }

    fun update(command: UpdateVoteEventCommand, refAt: LocalDateTime = LocalDateTime.now()): Mono<VoteEvent> {
        return voteEventRepository.findByIdOrThrow(command.id).map { voteEvent ->
            voteEvent.update(
                name = command.name,
                description = command.description,
                startAt = command.startAt,
                endAt = command.endAt,
                updatedAt = refAt,
            )
        }.flatMap {
            voteEventRepository.save(it)
        }
    }

    fun activate(id: Long, refAt: LocalDateTime = LocalDateTime.now()): Mono<VoteEvent> {
        return voteEventRepository.findByIdOrThrow(id)
            .map { it.activate(refAt) }
            .flatMap { voteEventRepository.save(it) }
    }

    fun close(id: Long, refAt: LocalDateTime = LocalDateTime.now()): Mono<VoteEvent> {
        return voteEventRepository.findByIdOrThrow(id)
            .map { it.close(refAt) }
            .flatMap { voteEventRepository.save(it) }
    }

    private fun VoteEventRepository.findByIdOrThrow(id: Long): Mono<VoteEvent> {
        val onError: Mono<VoteEvent> = Mono.error(VoteEventNotFoundException(id))
        return findById(id).switchIfEmpty(onError)
    }
}
