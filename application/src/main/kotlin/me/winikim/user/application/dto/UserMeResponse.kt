package me.winikim.user.application.dto

import me.winikim.security.application.service.JwtUser
import me.winikim.user.domain.enums.UserStatus
import me.winikim.user.domain.enums.AuthProvider

data class UserMeResponse(
    private val jwtUser: JwtUser
) {
    val status: UserStatus = jwtUser.status
    val id: Long = jwtUser.username.toLong()
    val authorities: List<String> = jwtUser.authorities.map { it.authority }
}