package com.siotman.vote.policy.infra

import com.siotman.vote.policy.domain.VotePolicy
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("vote_policy")
data class VotePolicyR2dbcEntity(
    @Id
    val id: Long? = null,
    @Column("name")
    val name: String,
    @Column("type")
    val type: String,
    @Column("params")
    val params: String,
    @Column("created_at")
    val createdAt: LocalDateTime,
    @Column("updated_at")
    val updatedAt: LocalDateTime,
) {
    fun toDomain(): VotePolicy {
        return VotePolicy(
            id = id,
            name = name,
            type = type,
            params = params,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
    }

    companion object {
        fun from(domain: VotePolicy): VotePolicyR2dbcEntity {
            return VotePolicyR2dbcEntity(
                id = domain.id,
                name = domain.name,
                type = domain.type,
                params = domain.params,
                createdAt = domain.createdAt,
                updatedAt = domain.updatedAt,
            )
        }
    }
}
