package com.siotman.vote.core.policy.domain

import java.time.LocalDateTime

class VotePolicy(
    val id: Long?,
    name: String,
    type: String,
    params: String,
    val createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
) {
    var name: String = name
        private set

    var type: String = type
        private set

    var params: String = params
        private set

    var updatedAt: LocalDateTime = updatedAt
        private set

    init {
        require(name.isNotBlank()) { "정책 이름은 비어 있을 수 없습니다." }
        require(type.isNotBlank()) { "정책 타입은 비어 있을 수 없습니다." }
        require(params.isNotBlank()) { "정책 파라미터는 비어 있을 수 없습니다." }
    }

    fun update(
        name: String,
        type: String,
        params: String,
        updatedAt: LocalDateTime,
    ): VotePolicy {
        require(name.isNotBlank()) { "정책 이름은 비어 있을 수 없습니다." }
        require(type.isNotBlank()) { "정책 타입은 비어 있을 수 없습니다." }
        require(params.isNotBlank()) { "정책 파라미터는 비어 있을 수 없습니다." }

        this.name = name
        this.type = type
        this.params = params
        this.updatedAt = updatedAt
        return this
    }
}
