@file:Suppress("NonAsciiCharacters")

package com.siotman.vote.core.voteevent.application

import com.siotman.vote.core.voteevent.support.TestVoteEventRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class VoteEventWriteServiceTest {
    private val voteEventRepository = TestVoteEventRepository()
    private val voteEventWriteService = VoteEventWriteService(voteEventRepository)

    @Test
    fun `관리자가 투표 이벤트를 생성하면 DRAFT 상태로 저장된다`() {
        val created = voteEventWriteService.create(
            CreateVoteEventCommand(
                campaignId = 1L,
                policyId = 10L,
                name = "5월 메인 이벤트",
                description = "월간 투표 이벤트",
                startAt = LocalDateTime.of(2026, 5, 1, 0, 0),
                endAt = LocalDateTime.of(2026, 5, 31, 23, 59),
                createdBy = "admin",
            ),
        ).block()!!

        assertThat(created.id).isNotNull
        assertThat(created.status.name).isEqualTo("DRAFT")
    }

    @Test
    fun `관리자가 투표 이벤트를 활성화할 수 있다`() {
        val created = voteEventWriteService.create(
            CreateVoteEventCommand(
                campaignId = 1L,
                policyId = 10L,
                name = "6월 메인 이벤트",
                description = null,
                startAt = LocalDateTime.of(2026, 6, 1, 0, 0),
                endAt = LocalDateTime.of(2026, 6, 30, 23, 59),
                createdBy = "admin",
            ),
        ).block()!!

        val activated = voteEventWriteService.activate(requireNotNull(created.id)).block()!!

        assertThat(activated.status.name).isEqualTo("ACTIVE")
    }

    @Test
    fun `관리자가 이벤트 이름 설명 기간을 수정할 수 있다`() {
        val created = voteEventWriteService.create(
            CreateVoteEventCommand(
                campaignId = null,
                policyId = 10L,
                name = "초기 이벤트명",
                description = "초기 설명",
                startAt = LocalDateTime.of(2026, 7, 1, 0, 0),
                endAt = LocalDateTime.of(2026, 7, 31, 23, 59),
                createdBy = "admin",
            ),
        ).block()!!

        val updated = voteEventWriteService.update(
            UpdateVoteEventCommand(
                id = requireNotNull(created.id),
                name = "수정 이벤트명",
                description = "수정 설명",
                startAt = LocalDateTime.of(2026, 7, 2, 0, 0),
                endAt = LocalDateTime.of(2026, 8, 1, 23, 59),
            ),
        ).block()!!

        assertThat(updated.name).isEqualTo("수정 이벤트명")
        assertThat(updated.description).isEqualTo("수정 설명")
        assertThat(updated.startAt).isEqualTo(LocalDateTime.of(2026, 7, 2, 0, 0))
        assertThat(updated.endAt).isEqualTo(LocalDateTime.of(2026, 8, 1, 23, 59))
    }
}
