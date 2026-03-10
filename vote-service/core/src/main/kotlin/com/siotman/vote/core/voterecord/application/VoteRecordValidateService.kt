package com.siotman.vote.core.voterecord.application

import com.siotman.vote.core.policy.domain.VotePolicy
import com.siotman.vote.core.policy.domain.spec.ChoicePolicy
import com.siotman.vote.core.policy.domain.spec.CompositePolicy
import com.siotman.vote.core.policy.domain.spec.PolicyEntry
import com.siotman.vote.core.policy.domain.spec.SingleChoicePolicy
import com.siotman.vote.core.policy.domain.spec.VotePolicySpec
import com.siotman.vote.core.voteevent.domain.VoteEvent
import com.siotman.vote.core.voteevent.domain.VoteEventStatus
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Service
class VoteRecordValidateService(
    private val voteRecordRepository: VoteRecordRepository,
) {
    fun validateParams(
        voteEvent: VoteEvent,
        votePolicy: VotePolicy,
        candidateIds: List<Long>,
        candidateIdsInEvent: List<Long>,
        userId: String,
        refAt: LocalDateTime,
    ): Mono<Void> {
        validateVoteEvent(voteEvent.status, voteEvent.startAt, voteEvent.endAt, refAt)
        validateCandidatesBelongToEvent(candidateIds, candidateIdsInEvent)
        validateVotePolicy(votePolicy.spec, candidateIds)

        return voteRecordRepository.findAllByEventIdAndUserId(voteEvent.id ?: 0L, userId)
            .hasElements()
            .flatMap { alreadyVoted ->
                if (alreadyVoted) {
                    Mono.error(VoteRecordValidationException("이미 투표한 사용자입니다. eventId=${voteEvent.id}, userId=$userId"))
                } else {
                    Mono.empty()
                }
            }
    }

    private fun validateVoteEvent(
        status: VoteEventStatus,
        startAt: LocalDateTime,
        endAt: LocalDateTime,
        refAt: LocalDateTime,
    ) {
        require(status == VoteEventStatus.ACTIVE) { "활성화된 이벤트에만 투표할 수 있습니다." }
        require(!refAt.isBefore(startAt) && !refAt.isAfter(endAt)) { "투표 가능 기간이 아닙니다." }
    }

    private fun validateCandidatesBelongToEvent(candidateIds: List<Long>, candidateIdsInEvent: List<Long>) {
        require(candidateIdsInEvent.isNotEmpty()) { "이벤트에 등록된 후보가 없습니다." }
        require(candidateIds.all { it in candidateIdsInEvent }) { "이벤트에 속하지 않은 후보가 포함되어 있습니다." }
    }

    private fun validateVotePolicy(spec: VotePolicySpec, candidateIds: List<Long>) {
        when (spec) {
            is SingleChoicePolicy -> validateChoiceCount(candidateIds, spec)
            is ChoicePolicy -> validateChoiceCount(candidateIds, spec)
            is CompositePolicy -> spec.policies
                .map(PolicyEntry::toSpec)
                .forEach { validateVotePolicy(it, candidateIds) }
        }
    }

    private fun validateChoiceCount(candidateIds: List<Long>, choicePolicy: ChoicePolicy) {
        val size = candidateIds.size
        require(size >= choicePolicy.minChoices) { "최소 ${choicePolicy.minChoices}개의 후보를 선택해야 합니다." }
        require(size <= choicePolicy.maxChoices) { "최대 ${choicePolicy.maxChoices}개의 후보만 선택할 수 있습니다." }
    }
}
