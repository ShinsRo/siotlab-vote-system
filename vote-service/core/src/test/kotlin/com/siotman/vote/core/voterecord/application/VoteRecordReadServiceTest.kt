package com.siotman.vote.core.voterecord.application

import com.siotman.vote.core.voterecord.support.TestVoteRecordRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class VoteRecordReadServiceTest {
    private val voteRecordRepository = TestVoteRecordRepository()
    private val voteRecordWriteService = VoteRecordWriteService(voteRecordRepository)
    private val voteRecordReadService = VoteRecordReadService(voteRecordRepository)

    @Test
    fun `존재하지 않는 투표 기록 조회 시 예외가 발생한다`() {
        assertThatThrownBy { voteRecordReadService.getById(999L).block() }
            .isInstanceOf(VoteRecordNotFoundException::class.java)
    }

    @Test
    fun `이벤트와 사용자 기준으로 투표 기록을 조회할 수 있다`() {
        voteRecordWriteService.create(
            CreateVoteRecordCommand(
                eventId = 1L,
                candidateIds = listOf(10L, 20L),
                userId = "user-1",
            ),
        ).collectList().block()!!

        voteRecordWriteService.create(
            CreateVoteRecordCommand(
                eventId = 1L,
                candidateIds = listOf(30L),
                userId = "user-2",
            ),
        ).collectList().block()!!

        val byEvent = voteRecordReadService.getByEventId(1L).collectList().block()!!
        val byEventAndUser = voteRecordReadService.getByEventIdAndUserId(1L, "user-1").collectList().block()!!

        assertThat(byEvent).hasSize(3)
        assertThat(byEventAndUser).hasSize(2)
        assertThat(byEventAndUser.map { it.userId }.distinct()).containsExactly("user-1")
    }
}
