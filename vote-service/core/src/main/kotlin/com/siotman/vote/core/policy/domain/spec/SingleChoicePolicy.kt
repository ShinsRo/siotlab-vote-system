package com.siotman.vote.core.policy.domain.spec

import com.siotman.vote.core.common.json.CoreJson

class SingleChoicePolicy : ChoicePolicy(minChoices = 1, maxChoices = 1) {
    companion object : VotePolicySpecMeta<SingleChoicePolicy> {
        override val type: String = SingleChoicePolicy::class.simpleName ?: "SingleChoicePolicy"
        override val jsonSchema: String by lazy { CoreJson.jsonSchema(SingleChoicePolicy::class.java) }
    }
}
