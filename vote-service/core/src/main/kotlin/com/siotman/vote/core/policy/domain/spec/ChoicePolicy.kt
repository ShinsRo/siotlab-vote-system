package com.siotman.vote.core.policy.domain.spec

abstract class ChoicePolicy(
    open val minChoices: Int,
    open val maxChoices: Int,
) : VotePolicySpec {
    override fun validateSpec() {
        require(minChoices > 0) { "minChoices는 0보다 커야 합니다." }
        require(maxChoices >= minChoices) { "maxChoices는 minChoices보다 크거나 같아야 합니다." }
    }
}
