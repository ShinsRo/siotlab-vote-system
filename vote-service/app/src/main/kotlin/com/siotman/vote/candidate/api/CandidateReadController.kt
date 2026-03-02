package com.siotman.vote.candidate.api

import com.siotman.vote.candidate.application.CandidateReadService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/candidates")
class CandidateReadController(
    private val candidateReadService: CandidateReadService,
) {
    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): Mono<CandidateResponse> {
        return candidateReadService.getById(id)
            .map(CandidateResponse::from)
    }

    @GetMapping
    fun list(): Flux<CandidateResponse> {
        return candidateReadService.getAll()
            .map(CandidateResponse::from)
    }
}
