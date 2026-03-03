package com.siotman.vote.core.candidate.application

import com.siotman.vote.core.candidate.domain.Candidate
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface CandidateRepository {
    fun save(candidate: Candidate): Mono<Candidate>
    fun findById(id: Long): Mono<Candidate>
    fun findAll(): Flux<Candidate>
    fun deleteById(id: Long): Mono<Void>
}
