package com.siotman.vote.adm.candidate

import com.siotman.vote.core.candidate.application.CandidateWriteService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
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
@Tag(name = "ADM Candidate", description = "관리자 후보 API")
class CandidateAdmController(
    private val candidateWriteService: CandidateWriteService,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "후보 생성")
    fun create(@RequestBody request: CreateCandidateRequest): Mono<CandidateResponse> {
        return candidateWriteService.create(request.toCommand())
            .map(CandidateResponse::from)
    }

    @PatchMapping("/{id}")
    @Operation(summary = "후보 수정")
    fun update(
        @PathVariable id: Long,
        @RequestBody request: UpdateCandidateRequest,
    ): Mono<CandidateResponse> {
        return candidateWriteService.update(request.toCommand(id))
            .map(CandidateResponse::from)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "후보 삭제")
    fun delete(@PathVariable id: Long): Mono<Void> {
        return candidateWriteService.delete(id)
    }
}
