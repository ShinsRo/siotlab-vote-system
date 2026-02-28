package com.siotman.vote.voteevent.application

class VoteEventNotFoundException(id: Long) : RuntimeException("투표 이벤트를 찾을 수 없습니다. id=$id")
