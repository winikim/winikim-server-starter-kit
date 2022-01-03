package me.winikim.user.infrastructure.external.auth

import me.winikim.user.domain.enums.AuthProvider
import me.winikim.user.infrastructure.external.auth.dto.TokenOutput
import me.winikim.user.infrastructure.external.auth.dto.UserInfoOutput

interface AuthExternalService {
    fun getToken(code: String): TokenOutput
    fun getUserInfo(accessToken: String): UserInfoOutput
    fun getAuthProvider(): AuthProvider
}