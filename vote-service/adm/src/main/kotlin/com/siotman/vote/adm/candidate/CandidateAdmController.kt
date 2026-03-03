package com.siotman.vote.adm.candidate

import com.siotman.vote.core.candidate.application.CandidateWriteService
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
    private val candidateWriteService: CandidateWriteService,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody request: CreateCandidateRequest): Mono<CandidateResponse> {
        return candidateWriteService.create(request.toCommand())
            .map(CandidateResponse::from)
    }

    @PatchMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody request: UpdateCandidateRequest,
    ): Mono<CandidateResponse> {
        return candidateWriteService.update(request.toCommand(id))
            .map(CandidateResponse::from)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long): Mono<Void> {
        return candidateWriteService.delete(id)
    }
}
