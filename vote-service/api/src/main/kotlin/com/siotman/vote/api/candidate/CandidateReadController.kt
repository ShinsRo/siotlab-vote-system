package com.siotman.vote.api.candidate

import com.siotman.vote.core.candidate.application.CandidateReadService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/candidates")
@Tag(name = "API Candidate", description = "후보 조회 API")
class CandidateReadController(
    private val candidateReadService: CandidateReadService,
) {
    @GetMapping("/{id}")
    @Operation(summary = "후보 단건 조회")
    fun get(@PathVariable id: Long): Mono<CandidateResponse> {
        return candidateReadService.getById(id)
            .map(CandidateResponse::from)
    }

    @GetMapping
    @Operation(summary = "후보 목록 조회")
    fun list(): Flux<CandidateResponse> {
        return candidateReadService.getAll()
            .map(CandidateResponse::from)
    }
}
