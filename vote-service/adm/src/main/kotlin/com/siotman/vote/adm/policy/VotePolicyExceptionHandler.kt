package com.siotman.vote.adm.policy

import com.siotman.vote.core.policy.application.VotePolicyNotFoundException
import com.siotman.vote.core.common.api.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class VotePolicyExceptionHandler {
    @ExceptionHandler(VotePolicyNotFoundException::class)
    fun handleNotFound(ex: VotePolicyNotFoundException): ResponseEntity<ApiResponse<Nothing>> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.failure(code = "vote_policy_not_found", message = ex.message ?: "투표 정책을 찾을 수 없습니다."))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(ex: IllegalArgumentException): ResponseEntity<ApiResponse<Nothing>> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.failure(code = "bad_request", message = ex.message ?: "잘못된 요청입니다."))
    }
}
