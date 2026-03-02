package com.siotman.vote.candidate.application

import com.siotman.vote.candidate.domain.Candidate
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class CandidateReadService(
    private val candidateRepository: CandidateRepository,
) {
    fun getById(id: Long): Mono<Candidate> {
        return candidateRepository.findById(id)
            .switchIfEmpty(Mono.error(CandidateNotFoundException(id)))
    }

    fun getAll(): Flux<Candidate> {
        return candidateRepository.findAll()
    }
}
