package com.siotman.vote.adm.voteevent

import com.siotman.vote.core.voteevent.application.VoteEventNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class VoteEventExceptionHandler {
    @ExceptionHandler(VoteEventNotFoundException::class)
    fun handleNotFound(ex: VoteEventNotFoundException): ResponseEntity<VoteEventErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(VoteEventErrorResponse(message = ex.message ?: "투표 이벤트를 찾을 수 없습니다."))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(ex: IllegalArgumentException): ResponseEntity<VoteEventErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(VoteEventErrorResponse(message = ex.message ?: "잘못된 요청입니다."))
    }
}
