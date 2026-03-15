package com.siotman.vote.gateway.auth.application

open class IdentityTranslationException(
    message: String,
    cause: Throwable? = null,
) : RuntimeException(message, cause)

class UnsupportedIssuerException(
    issuer: String,
) : IdentityTranslationException("Unsupported token issuer: $issuer")

class MissingAuthorizationException : IdentityTranslationException("Missing bearer access token")
