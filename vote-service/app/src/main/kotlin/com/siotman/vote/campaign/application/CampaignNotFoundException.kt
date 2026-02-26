package com.siotman.vote.campaign.application

class CampaignNotFoundException(id: Long) : RuntimeException("캠페인을 찾을 수 없습니다.")
