package com.siotman.vote.gateway.common.api

import com.siotman.vote.gateway.auth.application.IdentityTranslationException
import com.siotman.vote.gateway.auth.application.UnsupportedIssuerException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ServerWebInputException

@RestControllerAdvice
class GatewayApiExceptionHandler {
    @ExceptionHandler(UnsupportedIssuerException::class)
    fun handleUnsupportedIssuer(exception: UnsupportedIssuerException): ResponseEntity<ApiResponse<Nothing>> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.failure(code = "unsupported_issuer", message = exception.message ?: "지원하지 않는 issuer 입니다."))
    }

    @ExceptionHandler(ServerWebInputException::class, IllegalArgumentException::class)
    fun handleBadRequest(exception: Exception): ResponseEntity<ApiResponse<Nothing>> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.failure(code = "bad_request", message = exception.message ?: "잘못된 요청입니다."))
    }

    @ExceptionHandler(IdentityTranslationException::class)
    fun handleIdentityTranslation(exception: IdentityTranslationException): ResponseEntity<ApiResponse<Nothing>> {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.failure(code = "identity_translation_error", message = exception.message ?: "인증 변환 처리에 실패했습니다."))
    }
}
