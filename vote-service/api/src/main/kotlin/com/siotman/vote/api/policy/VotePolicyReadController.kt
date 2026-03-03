package com.siotman.vote.api.policy

import com.siotman.vote.core.policy.application.VotePolicyReadService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/policies")
@Tag(name = "API Vote Policy", description = "투표 정책 조회 API")
class VotePolicyReadController(
    private val votePolicyReadService: VotePolicyReadService,
) {
    @GetMapping("/{id}")
    @Operation(summary = "투표 정책 단건 조회")
    fun get(@PathVariable id: Long): Mono<VotePolicyResponse> {
        return votePolicyReadService.getById(id)
            .map(VotePolicyResponse::from)
    }

    @GetMapping
    @Operation(summary = "투표 정책 목록 조회")
    fun list(): Flux<VotePolicyResponse> {
        return votePolicyReadService.getAll()
            .map(VotePolicyResponse::from)
    }
}
