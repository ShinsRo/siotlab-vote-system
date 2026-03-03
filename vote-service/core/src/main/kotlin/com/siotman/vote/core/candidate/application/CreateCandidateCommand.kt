package com.siotman.vote.core.candidate.application

data class CreateCandidateCommand(
    val name: String,
    val description: String?,
    val imageUrl: String?,
)
