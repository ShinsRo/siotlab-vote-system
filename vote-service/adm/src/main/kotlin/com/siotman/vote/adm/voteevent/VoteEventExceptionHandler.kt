package com.siotman.vote.adm.voteevent

import com.siotman.vote.core.voteevent.application.VoteEventNotFoundException
import com.siotman.vote.core.common.api.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class VoteEventExceptionHandler {
    @ExceptionHandler(VoteEventNotFoundException::class)
    fun handleNotFound(ex: VoteEventNotFoundException): ResponseEntity<ApiResponse<Nothing>> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.failure(code = "vote_event_not_found", message = ex.message ?: "투표 이벤트를 찾을 수 없습니다."))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(ex: IllegalArgumentException): ResponseEntity<ApiResponse<Nothing>> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.failure(code = "bad_request", message = ex.message ?: "잘못된 요청입니다."))
    }
}
