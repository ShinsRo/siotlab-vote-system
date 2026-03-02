package com.siotman.vote.candidate.application

import com.siotman.vote.candidate.support.TestCandidateRepository
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class CandidateReadServiceTest {
    private val candidateRepository = TestCandidateRepository()
    private val candidateReadService = CandidateReadService(candidateRepository)

    @Test
    fun `존재하지 않는 후보 조회 시 예외가 발생한다`() {
        assertThatThrownBy { candidateReadService.getById(999L).block() }
            .isInstanceOf(CandidateNotFoundException::class.java)
    }
}
