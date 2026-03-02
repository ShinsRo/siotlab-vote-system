package com.siotman.vote.candidate.application

import com.siotman.vote.candidate.domain.Candidate
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Service
class CandidateAdmService(
    private val candidateRepository: CandidateRepository,
) {
    fun create(command: CreateCandidateCommand): Mono<Candidate> {
        val now = LocalDateTime.now()
        val candidate = Candidate(
            id = null,
            name = command.name,
            description = command.description,
            imageUrl = command.imageUrl,
            createdAt = now,
            updatedAt = now,
        )
        return candidateRepository.save(candidate)
    }

    fun update(command: UpdateCandidateCommand): Mono<Candidate> {
        return candidateRepository.findByIdOrThrow(command.id)
            .map {
                it.update(
                    name = command.name,
                    description = command.description,
                    imageUrl = command.imageUrl,
                    updatedAt = LocalDateTime.now(),
                )
            }
            .flatMap { candidateRepository.save(it) }
    }

    fun delete(id: Long): Mono<Void> {
        return candidateRepository.findByIdOrThrow(id)
            .flatMap { candidateRepository.deleteById(id) }
    }

    private fun CandidateRepository.findByIdOrThrow(id: Long): Mono<Candidate> {
        return findById(id)
            .switchIfEmpty(Mono.error(CandidateNotFoundException(id)))
    }
}
