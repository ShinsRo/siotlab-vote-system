package com.siotman.vote.core.voterecord.application

import com.siotman.vote.core.policy.application.VotePolicyReadService
import com.siotman.vote.core.policy.domain.spec.ChoicePolicy
import com.siotman.vote.core.policy.domain.spec.CompositePolicy
import com.siotman.vote.core.policy.domain.spec.PolicyEntry
import com.siotman.vote.core.policy.domain.spec.SingleChoicePolicy
import com.siotman.vote.core.policy.domain.spec.VotePolicySpec
import com.siotman.vote.core.policy.domain.spec.YesNoPolicy
import com.siotman.vote.core.voteevent.application.VoteEventReadService
import com.siotman.vote.core.voteevent.domain.VoteEventStatus
import com.siotman.vote.core.voterecord.domain.VoteRecord
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Service
class VoteRecordWriteService(
    private val voteRecordRepository: VoteRecordRepository,
    private val voteEventReadService: VoteEventReadService,
    private val votePolicyReadService: VotePolicyReadService,
    private val voteEventCandidateRepository: VoteEventCandidateRepository,
) {
    fun create(command: CreateVoteRecordCommand, refAt: LocalDateTime = LocalDateTime.now()): Flux<VoteRecord> {
        require(command.eventId > 0) { "eventId는 0보다 커야 합니다." }
        require(command.userId.isNotBlank()) { "userId는 비어 있을 수 없습니다." }

        val candidateIds = command.candidateIds.distinct()
        require(candidateIds.isNotEmpty()) { "candidateIds는 비어 있을 수 없습니다." }
        require(candidateIds.all { it > 0 }) { "candidateIds는 모두 0보다 커야 합니다." }

        return ensureNotVotedYet(command.eventId, command.userId)
            .then(voteEventReadService.getById(command.eventId))
            .flatMapMany { voteEvent ->
                validateVoteEvent(voteEvent.status, voteEvent.startAt, voteEvent.endAt, refAt)

                votePolicyReadService.getById(voteEvent.policyId)
                    .flatMapMany { votePolicy ->
                        validateVotePolicy(votePolicy.spec, candidateIds)

                        voteEventCandidateRepository.findCandidateIdsByEventId(command.eventId)
                            .collectList()
                            .flatMapMany { candidateIdsInEvent ->
                                validateCandidatesBelongToEvent(candidateIds, candidateIdsInEvent)

                                val voteRecords = candidateIds.map { candidateId ->
                                    VoteRecord(
                                        id = null,
                                        eventId = command.eventId,
                                        candidateId = candidateId,
                                        userId = command.userId,
                                        votedAt = refAt,
                                    )
                                }
                                voteRecordRepository.saveAll(voteRecords)
                            }
                    }
            }
    }

    private fun ensureNotVotedYet(eventId: Long, userId: String): Mono<Void> {
        return voteRecordRepository.findAllByEventIdAndUserId(eventId, userId)
            .hasElements()
            .flatMap { alreadyVoted ->
                if (alreadyVoted) {
                    Mono.error(VoteRecordValidationException("이미 투표한 사용자입니다. eventId=$eventId, userId=$userId"))
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
            is YesNoPolicy -> require(candidateIds.size == 1) { "찬반 투표는 하나의 선택만 가능합니다." }
            is CompositePolicy -> spec.policies
                .map(PolicyEntry::toSpec)
                .forEach { validateVotePolicy(it, candidateIds) }
            else -> throw VoteRecordValidationException("지원하지 않는 투표 정책입니다. type=${spec::class.simpleName}")
        }
    }

    private fun validateChoiceCount(candidateIds: List<Long>, choicePolicy: ChoicePolicy) {
        val size = candidateIds.size
        require(size >= choicePolicy.minChoices) { "최소 ${choicePolicy.minChoices}개의 후보를 선택해야 합니다." }
        require(size <= choicePolicy.maxChoices) { "최대 ${choicePolicy.maxChoices}개의 후보만 선택할 수 있습니다." }
    }
}
