package com.siotman.vote.core.voterecord.application

import com.siotman.vote.core.voterecord.support.TestVoteRecordRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class VoteRecordWriteServiceTest {
    private val voteRecordRepository = TestVoteRecordRepository()
    private val voteRecordWriteService = VoteRecordWriteService(voteRecordRepository)

    @Test
    fun `사용자가 여러 후보에 투표하면 candidateIds 수만큼 기록이 저장된다`() {
        val votedAt = LocalDateTime.of(2026, 3, 10, 18, 0, 0)

        val created = voteRecordWriteService.create(
            CreateVoteRecordCommand(
                eventId = 1L,
                candidateIds = listOf(10L, 20L),
                userId = "user-1",
            ),
            refAt = votedAt,
        ).collectList().block()!!

        assertThat(created).hasSize(2)
        assertThat(created.map { it.candidateId }).containsExactly(10L, 20L)
        assertThat(created.map { it.userId }.distinct()).containsExactly("user-1")
        assertThat(created.map { it.votedAt }.distinct()).containsExactly(votedAt)
    }

    @Test
    fun `중복 candidateId는 한 번만 저장한다`() {
        val created = voteRecordWriteService.create(
            CreateVoteRecordCommand(
                eventId = 1L,
                candidateIds = listOf(10L, 10L, 20L),
                userId = "user-1",
            ),
        ).collectList().block()!!

        assertThat(created).hasSize(2)
        assertThat(created.map { it.candidateId }).containsExactly(10L, 20L)
    }
}
