package me.winikim.user.infrastructure.external.auth.kakao.dto

data class KakaoTokenResponse(
    val token_type: String,
    val access_token: String,
    val expires_in: Long,
    val refresh_token: String,
    val refresh_token_expires_in: Long,
    val scope: String
)