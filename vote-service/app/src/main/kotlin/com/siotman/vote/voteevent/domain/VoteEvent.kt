package com.siotman.vote.voteevent.domain

import java.time.LocalDateTime

class VoteEvent(
    val id: Long?,
    val campaignId: Long?,
    val policyId: Long,
    name: String,
    description: String?,
    startAt: LocalDateTime,
    endAt: LocalDateTime,
    status: VoteEventStatus,
    val createdBy: String,
    val createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
) {
    var name: String = name
        private set

    var description: String? = description
        private set

    var startAt: LocalDateTime = startAt
        private set

    var endAt: LocalDateTime = endAt
        private set

    var status: VoteEventStatus = status
        private set

    var updatedAt: LocalDateTime = updatedAt
        private set

    init {
        require(policyId > 0) { "policyId는 0보다 커야 합니다." }
        require(name.isNotBlank()) { "이벤트 이름은 비어 있을 수 없습니다." }
        require(createdBy.isNotBlank()) { "생성자는 비어 있을 수 없습니다." }
        require(!endAt.isBefore(startAt)) { "이벤트 기간이 올바르지 않습니다. 종료일은 시작일 이후여야 합니다." }
    }

    fun update(
        name: String,
        description: String?,
        startAt: LocalDateTime,
        endAt: LocalDateTime,
        updatedAt: LocalDateTime,
    ): VoteEvent {
        require(status != VoteEventStatus.CLOSED) { "종료된 이벤트는 수정할 수 없습니다." }
        require(name.isNotBlank()) { "이벤트 이름은 비어 있을 수 없습니다." }
        require(!endAt.isBefore(startAt)) { "이벤트 기간이 올바르지 않습니다. 종료일은 시작일 이후여야 합니다." }

        this.name = name
        this.description = description
        this.startAt = startAt
        this.endAt = endAt
        this.updatedAt = updatedAt
        return this
    }

    fun activate(activatedAt: LocalDateTime): VoteEvent {
        require(status == VoteEventStatus.DRAFT) { "이벤트는 DRAFT 상태에서만 활성화할 수 있습니다." }
        status = VoteEventStatus.ACTIVE
        updatedAt = activatedAt
        return this
    }

    fun close(closedAt: LocalDateTime): VoteEvent {
        require(status != VoteEventStatus.CLOSED) { "이미 종료된 이벤트입니다." }
        status = VoteEventStatus.CLOSED
        updatedAt = closedAt
        return this
    }
}
