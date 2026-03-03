package com.siotman.vote.core.voteevent.application

import com.siotman.vote.core.voteevent.support.TestVoteEventRepository
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class VoteEventReadServiceTest {
    private val voteEventRepository = TestVoteEventRepository()
    private val voteEventReadService = VoteEventReadService(voteEventRepository)

    @Test
    fun `존재하지 않는 투표 이벤트 조회 시 예외가 발생한다`() {
        assertThatThrownBy { voteEventReadService.getById(999L).block() }
            .isInstanceOf(VoteEventNotFoundException::class.java)
    }
}
