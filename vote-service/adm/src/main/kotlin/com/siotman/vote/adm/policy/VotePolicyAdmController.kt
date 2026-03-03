package com.siotman.vote.adm.policy

import com.siotman.vote.core.policy.application.VotePolicyWriteService
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
@RequestMapping("/api/v1/adm/policies")
@Tag(name = "ADM Vote Policy", description = "관리자 투표 정책 API")
class VotePolicyAdmController(
    private val votePolicyWriteService: VotePolicyWriteService,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "투표 정책 생성")
    fun create(@RequestBody request: CreateVotePolicyRequest): Mono<VotePolicyResponse> {
        return votePolicyWriteService.create(request.toCommand())
            .map(VotePolicyResponse::from)
    }

    @PatchMapping("/{id}")
    @Operation(summary = "투표 정책 수정")
    fun update(
        @PathVariable id: Long,
        @RequestBody request: UpdateVotePolicyRequest,
    ): Mono<VotePolicyResponse> {
        return votePolicyWriteService.update(request.toCommand(id))
            .map(VotePolicyResponse::from)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "투표 정책 삭제")
    fun delete(@PathVariable id: Long): Mono<Void> {
        return votePolicyWriteService.delete(id)
    }
}
