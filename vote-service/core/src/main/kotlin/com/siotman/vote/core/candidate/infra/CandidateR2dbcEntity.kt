package com.siotman.vote.core.candidate.infra

import com.siotman.vote.core.candidate.domain.Candidate
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("vote_candidate")
data class CandidateR2dbcEntity(
    @Id
    val id: Long? = null,
    @Column("name")
    val name: String,
    @Column("description")
    val description: String?,
    @Column("image_url")
    val imageUrl: String?,
    @Column("created_at")
    val createdAt: LocalDateTime,
    @Column("updated_at")
    val updatedAt: LocalDateTime,
) {
    fun toDomain(): Candidate {
        return Candidate(
            id = id,
            name = name,
            description = description,
            imageUrl = imageUrl,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
    }

    companion object {
        fun from(domain: Candidate): CandidateR2dbcEntity {
            return CandidateR2dbcEntity(
                id = domain.id,
                name = domain.name,
                description = domain.description,
                imageUrl = domain.imageUrl,
                createdAt = domain.createdAt,
                updatedAt = domain.updatedAt,
            )
        }
    }
}
