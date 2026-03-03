package com.siotman.vote.core.policy.application

data class UpdateVotePolicyCommand(
    val id: Long,
    val name: String,
    val type: String,
    val params: String,
)
