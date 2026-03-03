package com.siotman.vote.core.candidate.application

data class UpdateCandidateCommand(
    val id: Long,
    val name: String,
    val description: String?,
    val imageUrl: String?,
)
