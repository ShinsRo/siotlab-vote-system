@file:Suppress("NonAsciiCharacters")

package com.siotman.vote.core.policy.domain.spec

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class VotePolicySpecSerdeTest {
    @Test
    fun `CompositePolicy를 type과 params 엔트리로 직렬화 역직렬화할 수 있다`() {
        val compositePolicy = CompositePolicy(
            policies = listOf(
                PolicyEntry.from(YesNoPolicy(allowAbstain = true)),
                PolicyEntry.from(MultipleChoicePolicy(minChoices = 1, maxChoices = 2)),
            ),
        )

        val type = VotePolicySpecSerde.typeOf(compositePolicy)
        val params = VotePolicySpecSerde.paramsOf(compositePolicy)
        val deserialized = VotePolicySpecSerde.deserialize(type, params)

        assertThat(type).isEqualTo(CompositePolicy.type)
        assertThat(deserialized).isInstanceOf(CompositePolicy::class.java)

        val restored = deserialized as CompositePolicy
        assertThat(restored.policies).hasSize(2)
        assertThat(restored.policies[0].type).isEqualTo(YesNoPolicy.type)
        assertThat(restored.policies[1].type).isEqualTo(MultipleChoicePolicy.type)

        restored.validateSpec()
    }
}
