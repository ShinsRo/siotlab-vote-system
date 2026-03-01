package com.siotman.vote.policy.application

class VotePolicyNotFoundException(id: Long) : RuntimeException("투표 정책을 찾을 수 없습니다. id=$id")
