package com.siotman.vote.core.candidate.domain

import java.time.LocalDateTime

class Candidate(
    val id: Long?,
    name: String,
    description: String?,
    imageUrl: String?,
    val createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
) {
    var name: String = name
        private set

    var description: String? = description
        private set

    var imageUrl: String? = imageUrl
        private set

    var updatedAt: LocalDateTime = updatedAt
        private set

    init {
        require(name.isNotBlank()) { "후보 이름은 비어 있을 수 없습니다." }
    }

    fun update(
        name: String,
        description: String?,
        imageUrl: String?,
        updatedAt: LocalDateTime,
    ): Candidate {
        require(name.isNotBlank()) { "후보 이름은 비어 있을 수 없습니다." }
        this.name = name
        this.description = description
        this.imageUrl = imageUrl
        this.updatedAt = updatedAt
        return this
    }
}
