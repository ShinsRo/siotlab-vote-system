@file:Suppress("NonAsciiCharacters")

package com.siotman.vote.core.voterecord.application

import com.siotman.vote.core.policy.application.VotePolicyReadService
import com.siotman.vote.core.policy.domain.VotePolicy
import com.siotman.vote.core.policy.domain.spec.MultipleChoicePolicy
import com.siotman.vote.core.policy.domain.spec.SingleChoicePolicy
import com.siotman.vote.core.voteevent.application.VoteEventReadService
import com.siotman.vote.core.voteevent.domain.VoteEvent
import com.siotman.vote.core.voteevent.domain.VoteEventStatus
import com.siotman.vote.core.voterecord.support.TestVoteEventCandidateRepository
import com.siotman.vote.core.voterecord.support.TestVoteEventRepositoryForVoteRecord
import com.siotman.vote.core.voterecord.support.TestVotePolicyRepositoryForVoteRecord
import com.siotman.vote.core.voterecord.support.TestVoteRecordRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class VoteRecordWriteServiceTest {
    private val voteRecordRepository = TestVoteRecordRepository()
    private val voteEventRepository = TestVoteEventRepositoryForVoteRecord()
    private val votePolicyRepository = TestVotePolicyRepositoryForVoteRecord()
    private val voteEventCandidateRepository = TestVoteEventCandidateRepository()
    private val voteRecordWriteService = VoteRecordWriteService(
        voteRecordRepository = voteRecordRepository,
        voteEventReadService = VoteEventReadService(voteEventRepository),
        votePolicyReadService = VotePolicyReadService(votePolicyRepository),
        voteEventCandidateRepository = voteEventCandidateRepository,
    )

    @Test
    fun `사용자가 여러 후보에 투표하면 candidateIds 수만큼 기록이 저장된다`() {
        val votedAt = LocalDateTime.of(2026, 3, 10, 18, 0, 0)
        givenActiveVoteEvent(policyId = 100L)
        givenVotePolicy(
            VotePolicy(
                id = 100L,
                name = "다중 선택",
                spec = MultipleChoicePolicy(minChoices = 1, maxChoices = 2),
                createdAt = votedAt.minusDays(1),
                updatedAt = votedAt.minusDays(1),
            ),
        )
        voteEventCandidateRepository.save(eventId = 1L, candidateIds = setOf(10L, 20L, 30L))

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
        val now = LocalDateTime.of(2026, 3, 10, 18, 0, 0)
        givenActiveVoteEvent(policyId = 100L, refAt = now)
        givenVotePolicy(
            VotePolicy(
                id = 100L,
                name = "다중 선택",
                spec = MultipleChoicePolicy(minChoices = 1, maxChoices = 2),
                createdAt = now.minusDays(1),
                updatedAt = now.minusDays(1),
            ),
        )
        voteEventCandidateRepository.save(eventId = 1L, candidateIds = setOf(10L, 20L))

        val created = voteRecordWriteService.create(
            CreateVoteRecordCommand(
                eventId = 1L,
                candidateIds = listOf(10L, 10L, 20L),
                userId = "user-1",
            ),
            refAt = now,
        ).collectList().block()!!

        assertThat(created).hasSize(2)
        assertThat(created.map { it.candidateId }).containsExactly(10L, 20L)
    }

    @Test
    fun `활성화되지 않은 이벤트에는 투표할 수 없다`() {
        val now = LocalDateTime.of(2026, 3, 10, 18, 0, 0)
        voteEventRepository.put(
            VoteEvent(
                id = 1L,
                campaignId = null,
                policyId = 100L,
                name = "draft event",
                description = null,
                startAt = now.minusHours(1),
                endAt = now.plusHours(1),
                status = VoteEventStatus.DRAFT,
                createdBy = "admin",
                createdAt = now.minusDays(1),
                updatedAt = now.minusDays(1),
            ),
        )
        givenVotePolicy(
            VotePolicy(
                id = 100L,
                name = "단일 선택",
                spec = SingleChoicePolicy(),
                createdAt = now.minusDays(1),
                updatedAt = now.minusDays(1),
            ),
        )
        voteEventCandidateRepository.save(eventId = 1L, candidateIds = setOf(10L))

        assertThatThrownBy {
            voteRecordWriteService.create(
                CreateVoteRecordCommand(1L, listOf(10L), "user-1"),
                refAt = now,
            ).collectList().block()
        }.hasMessageContaining("활성화된 이벤트")
    }

    @Test
    fun `이벤트에 속하지 않은 후보에는 투표할 수 없다`() {
        val now = LocalDateTime.of(2026, 3, 10, 18, 0, 0)
        givenActiveVoteEvent(policyId = 100L, refAt = now)
        givenVotePolicy(
            VotePolicy(
                id = 100L,
                name = "단일 선택",
                spec = SingleChoicePolicy(),
                createdAt = now.minusDays(1),
                updatedAt = now.minusDays(1),
            ),
        )
        voteEventCandidateRepository.save(eventId = 1L, candidateIds = setOf(10L))

        assertThatThrownBy {
            voteRecordWriteService.create(
                CreateVoteRecordCommand(1L, listOf(99L), "user-1"),
                refAt = now,
            ).collectList().block()
        }.hasMessageContaining("이벤트에 속하지 않은 후보")
    }

    @Test
    fun `단일 선택 정책에서는 한 명만 선택할 수 있다`() {
        val now = LocalDateTime.of(2026, 3, 10, 18, 0, 0)
        givenActiveVoteEvent(policyId = 100L, refAt = now)
        givenVotePolicy(
            VotePolicy(
                id = 100L,
                name = "단일 선택",
                spec = SingleChoicePolicy(),
                createdAt = now.minusDays(1),
                updatedAt = now.minusDays(1),
            ),
        )
        voteEventCandidateRepository.save(eventId = 1L, candidateIds = setOf(10L, 20L))

        assertThatThrownBy {
            voteRecordWriteService.create(
                CreateVoteRecordCommand(1L, listOf(10L, 20L), "user-1"),
                refAt = now,
            ).collectList().block()
        }.hasMessageContaining("최대 1개의 후보")
    }

    @Test
    fun `같은 사용자는 같은 이벤트에 중복 투표할 수 없다`() {
        val now = LocalDateTime.of(2026, 3, 10, 18, 0, 0)
        givenActiveVoteEvent(policyId = 100L, refAt = now)
        givenVotePolicy(
            VotePolicy(
                id = 100L,
                name = "단일 선택",
                spec = SingleChoicePolicy(),
                createdAt = now.minusDays(1),
                updatedAt = now.minusDays(1),
            ),
        )
        voteEventCandidateRepository.save(eventId = 1L, candidateIds = setOf(10L))

        voteRecordWriteService.create(
            CreateVoteRecordCommand(1L, listOf(10L), "user-1"),
            refAt = now,
        ).collectList().block()

        assertThatThrownBy {
            voteRecordWriteService.create(
                CreateVoteRecordCommand(1L, listOf(10L), "user-1"),
                refAt = now.plusMinutes(1),
            ).collectList().block()
        }.hasMessageContaining("이미 투표한 사용자")
    }

    private fun givenActiveVoteEvent(policyId: Long, refAt: LocalDateTime = LocalDateTime.of(2026, 3, 10, 18, 0, 0)) {
        voteEventRepository.put(
            VoteEvent(
                id = 1L,
                campaignId = null,
                policyId = policyId,
                name = "active event",
                description = null,
                startAt = refAt.minusHours(1),
                endAt = refAt.plusHours(1),
                status = VoteEventStatus.ACTIVE,
                createdBy = "admin",
                createdAt = refAt.minusDays(1),
                updatedAt = refAt.minusDays(1),
            ),
        )
    }

    private fun givenVotePolicy(votePolicy: VotePolicy) {
        votePolicyRepository.put(votePolicy)
    }
}
