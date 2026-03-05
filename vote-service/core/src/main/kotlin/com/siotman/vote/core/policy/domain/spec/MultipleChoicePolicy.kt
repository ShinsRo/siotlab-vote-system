package com.siotman.vote.core.policy.domain.spec

import com.siotman.vote.core.common.json.CoreJson

class MultipleChoicePolicy(
    override val minChoices: Int,
    override val maxChoices: Int,
) : ChoicePolicy(minChoices, maxChoices) {
    override fun validateSpec() {
        super.validateSpec()
    }

    companion object : VotePolicySpecMeta<MultipleChoicePolicy> {
        override val type: String = MultipleChoicePolicy::class.simpleName ?: "MultipleChoicePolicy"
        override val jsonSchema: String by lazy { CoreJson.jsonSchema(MultipleChoicePolicy::class.java) }
    }
}
