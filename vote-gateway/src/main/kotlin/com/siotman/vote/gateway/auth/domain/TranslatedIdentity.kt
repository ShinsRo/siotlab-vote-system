package com.siotman.vote.gateway.auth.domain

import java.time.Instant

/**
 * Identity Translation 레이어가 여러 외부 access token을 읽은 뒤
 * 다운스트림에 전달하기 전에 정규화하는 내부 표준 인증 컨텍스트다.
 *
 * principalId는 우리 시스템 내부 사용자 식별자를 의미한다.
 * 현재 vote-service 쪽 userId 모델은 String 이므로 숫자 ID를 쓰더라도 "101" 같은 문자열로 전달한다.
 * subject는 외부 issuer가 발급한 원본 주체 식별자를 유지하며, 예를 들면 "issuer-user-1" 같은 값을 가진다.
 */
data class TranslatedIdentity(
    val version: Int = 1,
    val principalId: String,
    val accountId: String? = null,
    val issuer: String,
    val subject: String,
    val roles: Set<IdentityRole> = emptySet(),
    val scopes: Set<String> = emptySet(),
    val tenantId: String? = null,
    val tokenId: String? = null,
    val issuedAt: Instant? = null,
    val expiresAt: Instant? = null,
    val attributes: Map<String, String> = emptyMap(),
)

enum class IdentityRole {
    USER,

    /**
     * ADMIN은 게이트웨이를 통과한 뒤 admin 성격의 다운스트림 리소스에 접근할 수 있는
     * 상위 역할을 의미한다. 외부 인증처마다 role 이름이 달라도 내부에서는 ADMIN으로 정규화한다.
     */
    ADMIN,
}
