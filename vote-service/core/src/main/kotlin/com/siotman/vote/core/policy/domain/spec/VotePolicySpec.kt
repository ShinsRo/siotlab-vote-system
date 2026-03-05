package com.siotman.vote.core.policy.domain.spec

interface VotePolicySpec {
    fun validateSpec()
}

interface VotePolicySpecMeta<T : VotePolicySpec> {
    val type: String
    val jsonSchema: String
}
