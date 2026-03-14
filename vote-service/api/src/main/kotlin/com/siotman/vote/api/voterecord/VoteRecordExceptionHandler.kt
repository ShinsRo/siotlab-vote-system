package com.siotman.vote.api.voterecord

import com.siotman.vote.core.voterecord.application.VoteRecordNotFoundException
import com.siotman.vote.core.voterecord.application.VoteRecordValidationException
import com.siotman.vote.core.common.api.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class VoteRecordExceptionHandler {
    @ExceptionHandler(VoteRecordNotFoundException::class)
    fun handleNotFound(exception: VoteRecordNotFoundException): ResponseEntity<ApiResponse<Nothing>> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.failure(code = "vote_record_not_found", message = exception.message ?: "투표 기록을 찾을 수 없습니다."))
    }

    @ExceptionHandler(VoteRecordValidationException::class, IllegalArgumentException::class)
    fun handleBadRequest(exception: RuntimeException): ResponseEntity<ApiResponse<Nothing>> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.failure(code = "vote_record_validation_failed", message = exception.message ?: "잘못된 투표 요청입니다."))
    }
}
