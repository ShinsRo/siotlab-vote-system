package com.siotman.vote.candidate.api

import com.siotman.vote.candidate.application.CandidateAdmService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/adm/candidates")
class CandidateAdmController(
    private val candidateAdmService: CandidateAdmService,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody request: CreateCandidateRequest): Mono<CandidateResponse> {
        return candidateAdmService.create(request.toCommand())
            .map(CandidateResponse::from)
    }

    @PatchMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody request: UpdateCandidateRequest,
    ): Mono<CandidateResponse> {
        return candidateAdmService.update(request.toCommand(id))
            .map(CandidateResponse::from)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long): Mono<Void> {
        return candidateAdmService.delete(id)
    }
}
