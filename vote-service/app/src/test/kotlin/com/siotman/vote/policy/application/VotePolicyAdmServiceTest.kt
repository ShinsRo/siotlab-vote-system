package com.siotman.vote.policy.application

import com.siotman.vote.policy.support.TestVotePolicyRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class VotePolicyAdmServiceTest {
    private val votePolicyRepository = TestVotePolicyRepository()
    private val votePolicyAdmService = VotePolicyAdmService(votePolicyRepository)

    @Test
    fun `관리자가 투표 정책을 생성할 수 있다`() {
        val created = votePolicyAdmService.create(
            CreateVotePolicyCommand(
                name = "1인 1표",
                type = "ONE_PER_USER",
                params = """{"maxVotes":1}""",
            ),
        ).block()!!

        assertThat(created.id).isNotNull
        assertThat(created.name).isEqualTo("1인 1표")
        assertThat(created.type).isEqualTo("ONE_PER_USER")
    }

    @Test
    fun `관리자가 투표 정책을 수정할 수 있다`() {
        val created = votePolicyAdmService.create(
            CreateVotePolicyCommand(
                name = "기존 정책",
                type = "LEGACY",
                params = """{"v":1}""",
            ),
        ).block()!!

        val updated = votePolicyAdmService.update(
            UpdateVotePolicyCommand(
                id = requireNotNull(created.id),
                name = "수정 정책",
                type = "UPDATED",
                params = """{"v":2}""",
            ),
        ).block()!!

        assertThat(updated.name).isEqualTo("수정 정책")
        assertThat(updated.type).isEqualTo("UPDATED")
        assertThat(updated.params).isEqualTo("""{"v":2}""")
    }
}
