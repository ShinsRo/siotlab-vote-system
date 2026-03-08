package com.siotman.vote.core.voterecord.application

import com.siotman.vote.core.voterecord.domain.VoteRecord
import com.siotman.vote.core.voterecord.support.TestVoteRecordRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class VoteRecordReadServiceTest {
    private val voteRecordRepository = TestVoteRecordRepository()
    private val voteRecordReadService = VoteRecordReadService(voteRecordRepository)

    @Test
    fun `존재하지 않는 투표 기록 조회 시 예외가 발생한다`() {
        assertThatThrownBy { voteRecordReadService.getById(999L).block() }
            .isInstanceOf(VoteRecordNotFoundException::class.java)
    }

    @Test
    fun `이벤트와 사용자 기준으로 투표 기록을 조회할 수 있다`() {
        voteRecordRepository.saveAll(
            listOf(
                VoteRecord(
                    id = null,
                    eventId = 1L,
                    candidateId = 10L,
                    userId = "user-1",
                    votedAt = LocalDateTime.of(2026, 3, 10, 18, 0, 0),
                ),
                VoteRecord(
                    id = null,
                    eventId = 1L,
                    candidateId = 20L,
                    userId = "user-1",
                    votedAt = LocalDateTime.of(2026, 3, 10, 18, 0, 0),
                ),
                VoteRecord(
                    id = null,
                    eventId = 1L,
                    candidateId = 30L,
                    userId = "user-2",
                    votedAt = LocalDateTime.of(2026, 3, 10, 18, 1, 0),
                ),
            ),
        ).collectList().block()!!

        val byEvent = voteRecordReadService.getByEventId(1L).collectList().block()!!
        val byEventAndUser = voteRecordReadService.getByEventIdAndUserId(1L, "user-1").collectList().block()!!

        assertThat(byEvent).hasSize(3)
        assertThat(byEventAndUser).hasSize(2)
        assertThat(byEventAndUser.map { it.userId }.distinct()).containsExactly("user-1")
    }
}
