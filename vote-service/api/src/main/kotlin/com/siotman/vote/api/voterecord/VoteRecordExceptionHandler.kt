package com.siotman.vote.api.voterecord

import com.siotman.vote.core.voterecord.application.VoteRecordNotFoundException
import com.siotman.vote.core.voterecord.application.VoteRecordValidationException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class VoteRecordExceptionHandler {
    @ExceptionHandler(VoteRecordNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFound(exception: VoteRecordNotFoundException): VoteRecordErrorResponse {
        return VoteRecordErrorResponse(message = exception.message ?: "투표 기록을 찾을 수 없습니다.")
    }

    @ExceptionHandler(VoteRecordValidationException::class, IllegalArgumentException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleBadRequest(exception: RuntimeException): VoteRecordErrorResponse {
        return VoteRecordErrorResponse(message = exception.message ?: "잘못된 투표 요청입니다.")
    }
}
