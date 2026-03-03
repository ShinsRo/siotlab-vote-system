package com.siotman.vote.core.policy.application

import com.siotman.vote.core.policy.support.TestVotePolicyRepository
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class VotePolicyReadServiceTest {
    private val votePolicyRepository = TestVotePolicyRepository()
    private val votePolicyReadService = VotePolicyReadService(votePolicyRepository)

    @Test
    fun `존재하지 않는 정책 조회 시 예외가 발생한다`() {
        assertThatThrownBy { votePolicyReadService.getById(999L).block() }
            .isInstanceOf(VotePolicyNotFoundException::class.java)
    }
}
