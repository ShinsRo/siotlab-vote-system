package com.siotman.vote.campaign.application

import com.siotman.vote.campaign.domain.Campaign
import com.siotman.vote.campaign.domain.CampaignStatus
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Service
class CampaignAdmService(
    private val campaignRepository: CampaignRepository,
) {
    fun create(command: CreateCampaignCommand): Mono<Campaign> {
        val campaign = command.toDomain()
        return campaignRepository.save(campaign)
    }

    fun close(id: Long): Mono<Campaign> {
        return campaignRepository.findByIdOrThrow(id)
            .map { it.close(LocalDateTime.now()) }
            .flatMap { campaignRepository.save(it) }
    }

    fun activate(id: Long): Mono<Campaign> {
        return campaignRepository.findByIdOrThrow(id)
            .map { it.activate(LocalDateTime.now()) }
            .flatMap { campaignRepository.save(it) }
    }

    fun update(command: UpdateCampaignCommand): Mono<Campaign> {
        return campaignRepository.findByIdOrThrow(command.id)
            .map {
                it.update(
                    name = command.name,
                    description = command.description,
                    startAt = command.startAt,
                    endAt = command.endAt,
                    updatedAt = LocalDateTime.now(),
                )
            }
            .flatMap { campaignRepository.save(it) }
    }

    private fun CreateCampaignCommand.toDomain(now: LocalDateTime = LocalDateTime.now()): Campaign {
        return Campaign(
            id = null,
            name = name,
            description = description,
            startAt = startAt,
            endAt = endAt,
            status = CampaignStatus.DRAFT,
            createdBy = createdBy,
            updatedAt = now,
            createdAt = now,
        )
    }

    private fun CampaignRepository.findByIdOrThrow(id: Long): Mono<Campaign> {
        return findById(id)
            .switchIfEmpty(Mono.error(CampaignNotFoundException(id)))
    }
}
