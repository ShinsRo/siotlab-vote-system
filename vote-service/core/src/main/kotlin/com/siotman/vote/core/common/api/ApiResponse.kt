package com.siotman.vote.core.common.api

import com.fasterxml.jackson.annotation.JsonInclude
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val error: ApiError? = null,
) {
    companion object {
        fun <T> success(data: T): ApiResponse<T> = ApiResponse(success = true, data = data)

        fun empty(): ApiResponse<Unit> = ApiResponse(success = true)

        fun failure(
            code: String,
            message: String,
        ): ApiResponse<Nothing> = ApiResponse(success = false, error = ApiError(code = code, message = message))
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiError(
    val code: String,
    val message: String,
)

fun <T : Any> Mono<T>.toApiResponse(): Mono<ApiResponse<T>> = map { ApiResponse.success(it) }

fun <T : Any> Flux<T>.toListApiResponse(): Mono<ApiResponse<List<T>>> = collectList().map { ApiResponse.success(it) }

fun Mono<Void>.toEmptyApiResponse(): Mono<ApiResponse<Unit>> = thenReturn(ApiResponse.empty())
