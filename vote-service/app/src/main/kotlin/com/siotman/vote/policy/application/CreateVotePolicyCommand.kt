package com.siotman.vote.policy.application

data class CreateVotePolicyCommand(
    val name: String,
    val type: String,
    val params: String,
)
