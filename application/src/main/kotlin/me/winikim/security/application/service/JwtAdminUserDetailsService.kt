package me.winikim.security.application.service

import me.winikim.adminuser.infrastructure.repository.AdminUserRepository
import me.winikim.share.doamin.exception.NotFoundUserException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service(value = "adminUserDetailsService")
class JwtAdminUserDetailsService(
    private val adminUserRepository: AdminUserRepository
): UserDetailsService {

    override fun loadUserByUsername(adminUserId: String): UserDetails {

        val adminUser = adminUserRepository.findByIdOrNull(adminUserId.toInt()) ?: throw NotFoundUserException()

        return JwtAdminUser(
            id = adminUser.id,
            authorities = mutableListOf(SimpleGrantedAuthority(adminUser.authorityName.name)),
            status = adminUser.status,
            username = adminUser.username
        )
    }
}