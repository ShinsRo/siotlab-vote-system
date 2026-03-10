package com.siotman.vote.core.voterecord.application

import com.siotman.vote.core.policy.application.VotePolicyReadService
import com.siotman.vote.core.voteevent.application.VoteEventReadService
import com.siotman.vote.core.voterecord.domain.VoteRecord
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.time.LocalDateTime

@Service
class VoteRecordWriteService(
    private val voteRecordRepository: VoteRecordRepository,
    private val voteEventReadService: VoteEventReadService,
    private val votePolicyReadService: VotePolicyReadService,
    private val voteRecordValidateService: VoteRecordValidateService,
    private val voteEventCandidateRepository: VoteEventCandidateRepository,
) {
    fun create(command: CreateVoteRecordCommand, refAt: LocalDateTime = LocalDateTime.now()): Flux<VoteRecord> {
        require(command.eventId > 0) { "eventId는 0보다 커야 합니다." }
        require(command.userId.isNotBlank()) { "userId는 비어 있을 수 없습니다." }

        val candidateIds = command.candidateIds.distinct()
        require(candidateIds.isNotEmpty()) { "candidateIds는 비어 있을 수 없습니다." }
        require(candidateIds.all { it > 0 }) { "candidateIds는 모두 0보다 커야 합니다." }

        val getVoteEvent = voteEventReadService.getById(command.eventId)
        val getCandidateIdsInEvent = voteEventCandidateRepository.findCandidateIdsByEventId(command.eventId)

        val getVotePolicy = getVoteEvent.zipWhen { voteEvent ->
            votePolicyReadService.getById(voteEvent.policyId)
        }

        val validateInput = getVotePolicy.zipWith(getCandidateIdsInEvent.collectList()).flatMap { tuple ->
            val voteEvent = tuple.t1.t1
            val votePolicy = tuple.t1.t2
            val candidateIdsInEvent = tuple.t2

            voteRecordValidateService.validateParams(
                voteEvent = voteEvent,
                votePolicy = votePolicy,
                candidateIds = candidateIds,
                candidateIdsInEvent = candidateIdsInEvent,
                userId = command.userId,
                refAt = refAt,
            )
        }

        // newVoteRecords 지연 생성 자체는 성능 상 크게 유의미하지 않다. 다른 Persist 로직에서 반드시 참고할 필요는 없음.
        val persistVoteRecords = Flux.defer {
            voteRecordRepository.saveAll(voteRecords = newVoteRecords(command, candidateIds, refAt))
        }

        return validateInput.thenMany(persistVoteRecords)
    }

    private fun newVoteRecords(
        command: CreateVoteRecordCommand,
        candidateIds: List<Long>,
        refAt: LocalDateTime,
    ): List<VoteRecord> {
        return candidateIds.map { candidateId ->
            VoteRecord(
                id = null,
                eventId = command.eventId,
                candidateId = candidateId,
                userId = command.userId,
                votedAt = refAt,
            )
        }
    }
}
