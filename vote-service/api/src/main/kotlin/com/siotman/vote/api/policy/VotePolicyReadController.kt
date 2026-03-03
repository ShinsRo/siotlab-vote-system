package com.siotman.vote.api.policy

import com.siotman.vote.core.policy.application.VotePolicyReadService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/policies")
class VotePolicyReadController(
    private val votePolicyReadService: VotePolicyReadService,
) {
    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): Mono<VotePolicyResponse> {
        return votePolicyReadService.getById(id)
            .map(VotePolicyResponse::from)
    }

    @GetMapping
    fun list(): Flux<VotePolicyResponse> {
        return votePolicyReadService.getAll()
            .map(VotePolicyResponse::from)
    }
}
