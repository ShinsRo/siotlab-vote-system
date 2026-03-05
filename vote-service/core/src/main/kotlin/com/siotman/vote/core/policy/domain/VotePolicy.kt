package com.siotman.vote.core.policy.domain

import com.siotman.vote.core.policy.domain.spec.VotePolicySpec
import com.siotman.vote.core.policy.domain.spec.VotePolicySpecSerde
import java.time.LocalDateTime

class VotePolicy(
    val id: Long?,
    name: String,
    spec: VotePolicySpec,
    val createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
) {
    var name: String = name
        private set

    var spec: VotePolicySpec = spec
        private set

    var updatedAt: LocalDateTime = updatedAt
        private set

    val type: String
        get() = VotePolicySpecSerde.typeOf(spec)

    val params: String
        get() = VotePolicySpecSerde.paramsOf(spec)

    init {
        require(name.isNotBlank()) { "정책 이름은 비어 있을 수 없습니다." }
        spec.validateSpec()
    }

    fun update(
        name: String,
        spec: VotePolicySpec,
        updatedAt: LocalDateTime,
    ): VotePolicy {
        require(name.isNotBlank()) { "정책 이름은 비어 있을 수 없습니다." }
        spec.validateSpec()

        this.name = name
        this.spec = spec
        this.updatedAt = updatedAt
        return this
    }
}
