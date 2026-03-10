package com.siotman.vote.core.policy.domain.spec

import com.fasterxml.jackson.databind.JsonNode
import com.siotman.vote.core.common.json.CoreJson

data class CompositePolicy(
    val policies: List<PolicyEntry>,
) : VotePolicySpec() {
    override fun validateSpec() {
        require(policies.isNotEmpty()) { "policies는 비어 있을 수 없습니다." }
        policies
            .map(PolicyEntry::toSpec)
            .forEach(VotePolicySpec::validateSpec)
    }

    companion object : VotePolicySpecMeta<CompositePolicy> {
        override val type: String = CompositePolicy::class.simpleName ?: "CompositePolicy"
        override val jsonSchema: String by lazy { CoreJson.jsonSchema(CompositePolicy::class.java) }
    }
}

data class PolicyEntry(
    val type: String,
    val params: JsonNode,
) {
    fun toSpec(): VotePolicySpec {
        return VotePolicySpecSerde.deserialize(
            type = type,
            params = CoreJson.objectMapper.writeValueAsString(params),
        )
    }

    companion object {
        fun from(spec: VotePolicySpec): PolicyEntry {
            return PolicyEntry(
                type = VotePolicySpecSerde.typeOf(spec),
                params = CoreJson.objectMapper.valueToTree(spec),
            )
        }
    }
}
