package me.winikim.user.application.dto

import me.winikim.user.domain.enums.AuthProvider


data class SignInRequest(
    val authProvider: AuthProvider,
    val authToken: String,
)