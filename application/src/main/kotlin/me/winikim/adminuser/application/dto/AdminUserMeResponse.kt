package me.winikim.adminuser.application.dto

import me.winikim.security.application.service.JwtAdminUser

data class AdminUserMeResponse(
    private val jwtAdminUser: JwtAdminUser
) {
    val status = jwtAdminUser.status
    val id = jwtAdminUser.id
    val username = jwtAdminUser.getUsernameInService()
    val authorities: List<String> = jwtAdminUser.authorities.map { it.authority }
}