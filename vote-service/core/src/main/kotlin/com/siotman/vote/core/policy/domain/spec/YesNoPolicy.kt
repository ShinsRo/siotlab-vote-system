package com.siotman.vote.core.policy.domain.spec

import com.siotman.vote.core.common.json.CoreJson

data class YesNoPolicy(
    val allowAbstain: Boolean = false,
) : VotePolicySpec {
    override fun validateSpec() {
    }

    companion object : VotePolicySpecMeta<YesNoPolicy> {
        override val type: String = YesNoPolicy::class.simpleName ?: "YesNoPolicy"
        override val jsonSchema: String by lazy { CoreJson.jsonSchema(YesNoPolicy::class.java) }
    }
}
