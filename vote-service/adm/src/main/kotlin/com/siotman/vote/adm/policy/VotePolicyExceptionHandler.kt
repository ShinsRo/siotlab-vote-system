package com.siotman.vote.adm.policy

import com.siotman.vote.core.policy.application.VotePolicyNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class VotePolicyExceptionHandler {
    @ExceptionHandler(VotePolicyNotFoundException::class)
    fun handleNotFound(ex: VotePolicyNotFoundException): ResponseEntity<VotePolicyErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(VotePolicyErrorResponse(message = ex.message ?: "투표 정책을 찾을 수 없습니다."))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(ex: IllegalArgumentException): ResponseEntity<VotePolicyErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(VotePolicyErrorResponse(message = ex.message ?: "잘못된 요청입니다."))
    }
}
