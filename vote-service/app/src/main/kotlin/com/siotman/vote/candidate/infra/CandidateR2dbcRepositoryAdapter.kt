package com.siotman.vote.candidate.infra

import com.siotman.vote.candidate.application.CandidateRepository
import com.siotman.vote.candidate.domain.Candidate
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class CandidateR2dbcRepositoryAdapter(
    private val candidateR2dbcRepository: CandidateR2dbcRepository,
) : CandidateRepository {
    override fun save(candidate: Candidate): Mono<Candidate> {
        return candidateR2dbcRepository.save(CandidateR2dbcEntity.from(candidate))
            .map { it.toDomain() }
    }

    override fun findById(id: Long): Mono<Candidate> {
        return candidateR2dbcRepository.findById(id)
            .map { it.toDomain() }
    }

    override fun findAll(): Flux<Candidate> {
        return candidateR2dbcRepository.findAllByOrderByCreatedAtDesc()
            .map { it.toDomain() }
    }

    override fun deleteById(id: Long): Mono<Void> {
        return candidateR2dbcRepository.deleteById(id)
    }
}
