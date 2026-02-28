package com.siotman.vote.voteevent.application

import java.time.LocalDateTime

data class CreateVoteEventCommand(
    val campaignId: Long?,
    val policyId: Long,
    val name: String,
    val description: String?,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
    val createdBy: String,
)
