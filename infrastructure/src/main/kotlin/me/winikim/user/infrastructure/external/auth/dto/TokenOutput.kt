package me.winikim.user.infrastructure.external.auth.dto

data class TokenOutput(
    val tokenType: String,
    val accessToken: String,
    val expiresIn: Long,
    val refreshToken: String,
    val refreshTokenExpiresIn: Long
)