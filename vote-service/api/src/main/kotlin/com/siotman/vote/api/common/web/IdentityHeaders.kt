package com.siotman.vote.api.common.web

const val PRINCIPAL_ID_HEADER: String = "X-Identity-Principal-Id"

fun requirePrincipalId(principalId: String?): String {
    require(!principalId.isNullOrBlank()) { "$PRINCIPAL_ID_HEADER 헤더가 필요합니다." }
    return principalId
}
