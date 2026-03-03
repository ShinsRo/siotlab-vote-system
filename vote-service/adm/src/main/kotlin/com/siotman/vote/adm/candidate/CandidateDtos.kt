package com.siotman.vote.adm.candidate

import com.siotman.vote.core.candidate.application.CreateCandidateCommand
import com.siotman.vote.core.candidate.application.UpdateCandidateCommand
import com.siotman.vote.core.candidate.domain.Candidate
import java.time.LocalDateTime

data class CreateCandidateRequest(
    val name: String,
    val description: String?,
    val imageUrl: String?,
) {
    fun toCommand(): CreateCandidateCommand {
        return CreateCandidateCommand(
            name = name,
            description = description,
            imageUrl = imageUrl,
        )
    }
}

data class UpdateCandidateRequest(
    val name: String,
    val description: String?,
    val imageUrl: String?,
) {
    fun toCommand(id: Long): UpdateCandidateCommand {
        return UpdateCandidateCommand(
            id = id,
            name = name,
            description = description,
            imageUrl = imageUrl,
        )
    }
}

data class CandidateResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val imageUrl: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    companion object {
        fun from(candidate: Candidate): CandidateResponse {
            return CandidateResponse(
                id = requireNotNull(candidate.id) { "후보 ID는 null일 수 없습니다." },
                name = candidate.name,
                description = candidate.description,
                imageUrl = candidate.imageUrl,
                createdAt = candidate.createdAt,
                updatedAt = candidate.updatedAt,
            )
        }
    }
}

data class CandidateErrorResponse(
    val message: String,
)
