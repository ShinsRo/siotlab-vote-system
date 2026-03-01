package com.siotman.vote.policy.api

import com.siotman.vote.policy.application.VotePolicyAdmService
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
class VotePolicyAdmController(
    private val votePolicyAdmService: VotePolicyAdmService,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody request: CreateVotePolicyRequest): Mono<VotePolicyResponse> {
        return votePolicyAdmService.create(request.toCommand())
            .map(VotePolicyResponse::from)
    }

    @PatchMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody request: UpdateVotePolicyRequest,
    ): Mono<VotePolicyResponse> {
        return votePolicyAdmService.update(request.toCommand(id))
            .map(VotePolicyResponse::from)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long): Mono<Void> {
        return votePolicyAdmService.delete(id)
    }
}
