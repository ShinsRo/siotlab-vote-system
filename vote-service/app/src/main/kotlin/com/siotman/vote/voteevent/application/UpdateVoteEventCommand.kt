package com.siotman.vote.voteevent.application

import java.time.LocalDateTime

data class UpdateVoteEventCommand(
    val id: Long,
    val name: String,
    val description: String?,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
)
