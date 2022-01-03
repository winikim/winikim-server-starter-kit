package me.winikim.user.application.dto

import me.winikim.user.domain.enums.UserStatus

data class SignInResponse(
    val username: String,
    val status: UserStatus,
    val accessToken: String
)