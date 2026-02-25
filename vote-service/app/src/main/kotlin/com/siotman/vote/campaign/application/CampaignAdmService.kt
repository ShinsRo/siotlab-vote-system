package com.siotman.vote.campaign.application

import com.siotman.vote.campaign.domain.Campaign
import com.siotman.vote.campaign.domain.CampaignStatus
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CampaignAdmService(
    private val campaignRepository: CampaignRepository,
) {
    fun create(command: CreateCampaignCommand): Campaign {
        val campaign = command.toDomain()
        return campaignRepository.save(campaign)
    }

    fun close(id: Long): Campaign {
        val campaign = campaignRepository.findByIdOrThrow(id)
        val closed = campaign.close(LocalDateTime.now())
        return campaignRepository.save(closed)
    }

    fun activate(id: Long): Campaign {
        val campaign = campaignRepository.findByIdOrThrow(id)
        val activated = campaign.activate(LocalDateTime.now())
        return campaignRepository.save(activated)
    }

    fun CreateCampaignCommand.toDomain(now: LocalDateTime = LocalDateTime.now()): Campaign {
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

    fun CampaignRepository.findByIdOrThrow(id: Long): Campaign {
        return findById(id) ?: throw CampaignNotFoundException(id)
    }
}
