package com.siotman.vote.api.candidate

import com.siotman.vote.core.candidate.application.CandidateNotFoundException
import com.siotman.vote.core.common.api.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CandidateExceptionHandler {
    @ExceptionHandler(CandidateNotFoundException::class)
    fun handleNotFound(ex: CandidateNotFoundException): ResponseEntity<ApiResponse<Nothing>> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.failure(code = "candidate_not_found", message = ex.message ?: "후보를 찾을 수 없습니다."))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(ex: IllegalArgumentException): ResponseEntity<ApiResponse<Nothing>> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.failure(code = "bad_request", message = ex.message ?: "잘못된 요청입니다."))
    }
}
