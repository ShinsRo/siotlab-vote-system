package com.siotman.vote.core.voterecord.application

class VoteRecordNotFoundException(id: Long) : RuntimeException("투표 기록을 찾을 수 없습니다. id=$id")
