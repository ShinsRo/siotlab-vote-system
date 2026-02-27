package com.siotman.vote.campaign.application

class CampaignNotFoundException(id: Long) : RuntimeException("campaign not found: id=$id")
