package me.winikim.adminuser.application.service

import me.winikim.adminuser.application.dto.*
import me.winikim.adminuser.domain.entity.AdminUser
import me.winikim.adminuser.domain.enums.AdminUserStatus
import me.winikim.adminuser.infrastructure.repository.AdminUserRepository
import me.winikim.security.application.service.AdminJwtTokenProvider
import me.winikim.security.application.service.JwtAdminUser
import me.winikim.share.doamin.exception.CheckUsernameOrPasswordException
import me.winikim.share.doamin.exception.NotFoundAdminUserException
import me.winikim.share.doamin.exception.UsernameAlreadyExistsException
import net.yayoung.share.application.dto.PageResponse
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class AdminUserService(
    private val adminUserRepository: AdminUserRepository,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
    private val adminJwtTokenProvider: AdminJwtTokenProvider
) {
    fun signIn(adminSignInRequest: AdminSignInRequest): AdminSignInResponse {
        val adminUser =
            adminUserRepository.findByUsername(adminSignInRequest.username) ?: throw NotFoundAdminUserException()
        if (!bCryptPasswordEncoder.matches(
                adminSignInRequest.password,
                adminUser.password
            )
        ) throw CheckUsernameOrPasswordException()

        val accessToken = adminJwtTokenProvider.generateToken(
            JwtAdminUser(
                id = adminUser.id,
                status = adminUser.status,
                authorities = mutableListOf(SimpleGrantedAuthority(adminUser.authorityName.name)),
                username = adminUser.username
            )
        )

        return AdminSignInResponse(
            accessToken = accessToken
        )
    }

    fun createAdminUser(createAdminUserRequest: CreateAdminUserRequest) {
        if (adminUserRepository.existsByUsername(createAdminUserRequest.username)) throw UsernameAlreadyExistsException()
        val encryptedPassword = bCryptPasswordEncoder.encode(createAdminUserRequest.password)
        adminUserRepository.save(
            AdminUser(
                username = createAdminUserRequest.username,
                password = encryptedPassword,
                status = AdminUserStatus.ACTIVE
            )
        )
    }

    fun retrieveAdminUsers(pageable: Pageable): PageResponse {
        val page = adminUserRepository.findAll(pageable)
        return PageResponse(
            totalElements = page.totalElements,
            page = page.number,
            size = page.size,
            elements = page.content.map {
                AdminUserResponse(
                    id = it.id,
                    username = it.username
                )
            }
        )
    }

    fun deleteAdminUser(adminUserId: Int) {
        val adminUser = adminUserRepository.findByIdOrNull(adminUserId) ?: throw NotFoundAdminUserException()
        adminUserRepository.delete(adminUser)
    }
}