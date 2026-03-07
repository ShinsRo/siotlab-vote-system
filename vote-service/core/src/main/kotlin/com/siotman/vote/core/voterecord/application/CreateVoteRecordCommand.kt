package com.siotman.vote.core.voterecord.application

data class CreateVoteRecordCommand(
    val eventId: Long,
    val candidateIds: List<Long>,
    val userId: String,
)
