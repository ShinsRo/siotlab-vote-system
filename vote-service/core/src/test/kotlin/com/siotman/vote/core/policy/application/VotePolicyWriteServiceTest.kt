@file:Suppress("NonAsciiCharacters")

package com.siotman.vote.core.policy.application

import com.siotman.vote.core.policy.domain.spec.MultipleChoicePolicy
import com.siotman.vote.core.policy.domain.spec.YesNoPolicy
import com.siotman.vote.core.policy.support.TestVotePolicyRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class VotePolicyWriteServiceTest {
    private val votePolicyRepository = TestVotePolicyRepository()
    private val votePolicyWriteService = VotePolicyWriteService(votePolicyRepository)

    @Test
    fun `관리자가 투표 정책을 생성할 수 있다`() {
        val created = votePolicyWriteService.create(
            CreateVotePolicyCommand(
                name = "1인 1표",
                type = YesNoPolicy.type,
                params = """{"allowAbstain":false}""",
            ),
        ).block()!!

        assertThat(created.id).isNotNull
        assertThat(created.name).isEqualTo("1인 1표")
        assertThat(created.type).isEqualTo(YesNoPolicy.type)
    }

    @Test
    fun `관리자가 투표 정책을 수정할 수 있다`() {
        val created = votePolicyWriteService.create(
            CreateVotePolicyCommand(
                name = "기존 정책",
                type = YesNoPolicy.type,
                params = """{"allowAbstain":true}""",
            ),
        ).block()!!

        val updated = votePolicyWriteService.update(
            UpdateVotePolicyCommand(
                id = requireNotNull(created.id),
                name = "수정 정책",
                type = MultipleChoicePolicy.type,
                params = """{"minChoices":1,"maxChoices":2}""",
            ),
        ).block()!!

        assertThat(updated.name).isEqualTo("수정 정책")
        assertThat(updated.type).isEqualTo(MultipleChoicePolicy.type)
        assertThat(updated.params).isEqualTo("""{"minChoices":1,"maxChoices":2}""")
    }
}
