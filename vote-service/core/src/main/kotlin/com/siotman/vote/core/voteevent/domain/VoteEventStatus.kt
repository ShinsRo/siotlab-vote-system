package com.siotman.vote.core.voteevent.domain

enum class VoteEventStatus(val label: String) {
    DRAFT("초안"),
    ACTIVE("활성"),
    CLOSED("종료"),
}
