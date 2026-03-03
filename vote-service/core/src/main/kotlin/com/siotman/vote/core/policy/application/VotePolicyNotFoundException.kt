package com.siotman.vote.core.policy.application

class VotePolicyNotFoundException(id: Long) : RuntimeException("투표 정책을 찾을 수 없습니다. id=$id")
