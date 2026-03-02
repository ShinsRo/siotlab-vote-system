package com.siotman.vote.candidate.application

import com.siotman.vote.candidate.support.TestCandidateRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CandidateAdmServiceTest {
    private val candidateRepository = TestCandidateRepository()
    private val candidateAdmService = CandidateAdmService(candidateRepository)

    @Test
    fun `관리자가 후보를 생성할 수 있다`() {
        val created = candidateAdmService.create(
            CreateCandidateCommand(
                name = "후보 A",
                description = "설명 A",
                imageUrl = "https://example.com/a.png",
            ),
        ).block()!!

        assertThat(created.id).isNotNull
        assertThat(created.name).isEqualTo("후보 A")
    }

    @Test
    fun `관리자가 후보를 수정할 수 있다`() {
        val created = candidateAdmService.create(
            CreateCandidateCommand(
                name = "초기 후보",
                description = "초기 설명",
                imageUrl = null,
            ),
        ).block()!!

        val updated = candidateAdmService.update(
            UpdateCandidateCommand(
                id = requireNotNull(created.id),
                name = "수정 후보",
                description = "수정 설명",
                imageUrl = "https://example.com/new.png",
            ),
        ).block()!!

        assertThat(updated.name).isEqualTo("수정 후보")
        assertThat(updated.description).isEqualTo("수정 설명")
        assertThat(updated.imageUrl).isEqualTo("https://example.com/new.png")
    }
}
