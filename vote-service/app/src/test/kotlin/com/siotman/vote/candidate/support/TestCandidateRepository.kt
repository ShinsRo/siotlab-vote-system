package com.siotman.vote.candidate.support

import com.siotman.vote.candidate.application.CandidateRepository
import com.siotman.vote.candidate.domain.Candidate
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

class TestCandidateRepository : CandidateRepository {
    private val sequence = AtomicLong(0)
    private val candidates = ConcurrentHashMap<Long, Candidate>()

    override fun save(candidate: Candidate): Mono<Candidate> {
        val persisted = if (candidate.id == null) {
            Candidate(
                id = sequence.incrementAndGet(),
                name = candidate.name,
                description = candidate.description,
                imageUrl = candidate.imageUrl,
                createdAt = candidate.createdAt,
                updatedAt = candidate.updatedAt,
            )
        } else {
            candidate
        }
        candidates[requireNotNull(persisted.id)] = persisted
        return Mono.just(persisted)
    }

    override fun findById(id: Long): Mono<Candidate> {
        val candidate = candidates[id] ?: return Mono.empty()
        return Mono.just(candidate)
    }

    override fun findAll(): Flux<Candidate> {
        return Flux.fromIterable(candidates.values.sortedByDescending { it.createdAt })
    }

    override fun deleteById(id: Long): Mono<Void> {
        candidates.remove(id)
        return Mono.empty()
    }
}
