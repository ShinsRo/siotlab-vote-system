package com.siotman.vote.core.policy.domain.spec

sealed class VotePolicySpec {
    abstract fun validateSpec()
}

interface VotePolicySpecMeta<T : VotePolicySpec> {
    val type: String
    val jsonSchema: String
}
