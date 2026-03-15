package com.siotman.vote.gateway.auth.application

open class IdentityTranslationException(
    message: String,
    cause: Throwable? = null,
) : RuntimeException(message, cause)

class UnsupportedIssuerException(
    issuer: String,
) : IdentityTranslationException("지원하지 않는 토큰 issuer 입니다: $issuer")

class MissingAuthorizationException : IdentityTranslationException("Bearer 액세스 토큰이 없습니다.")
