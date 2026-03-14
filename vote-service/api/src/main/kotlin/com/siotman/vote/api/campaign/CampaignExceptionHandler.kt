package com.siotman.vote.api.campaign

import com.siotman.vote.core.campaign.application.CampaignNotFoundException
import com.siotman.vote.core.common.api.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CampaignExceptionHandler {
    @ExceptionHandler(CampaignNotFoundException::class)
    fun handleNotFound(ex: CampaignNotFoundException): ResponseEntity<ApiResponse<Nothing>> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.failure(code = "campaign_not_found", message = "캠페인을 찾을 수 없습니다."))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(ex: IllegalArgumentException): ResponseEntity<ApiResponse<Nothing>> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.failure(code = "bad_request", message = "잘못된 요청입니다."))
    }
}
