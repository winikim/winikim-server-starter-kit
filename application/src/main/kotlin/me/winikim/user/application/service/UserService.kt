package me.winikim.user.application.service

import me.winikim.user.application.dto.SignInRequest
import me.winikim.user.application.dto.SignInResponse
import me.winikim.security.application.service.JwtUser
import me.winikim.security.application.service.JwtTokenProvider
import me.winikim.user.domain.entity.User
import me.winikim.user.domain.enums.AuthProvider
import me.winikim.user.domain.repository.UserRepository
import me.winikim.user.infrastructure.external.auth.AuthExternalServiceFactory
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service

@Service
class UserService(
    private val authExternalServiceFactory: AuthExternalServiceFactory,
    private val userRepository: UserRepository,
    private val jwtTokenProvider: JwtTokenProvider
) {
    fun signIn(signInRequest: SignInRequest): SignInResponse {
        val authService = authExternalServiceFactory.getService(signInRequest.authProvider)
        val userInfo = authService.getUserInfo(signInRequest.authToken)
        val username = signInRequest.authProvider.name[0] + userInfo.id
        val user = userRepository.findByUsername(username) ?: this.signUp(
            userInfo.id,
            signInRequest.authProvider,
        )
        return SignInResponse(
            username = user.username,
            status = user.status,
            accessToken = jwtTokenProvider.generateToken(
                JwtUser(
                    id = user.id,
                    status = user.status,
                    authorities = user.userAuthorities.map { SimpleGrantedAuthority(it.authority.authorityName.name) }
                        .toMutableList()
                )
            )
        )
    }

    private fun signUp(authProviderId: String, authProvider: AuthProvider): User {
        return userRepository.save(
            User(
                authProviderId = authProviderId,
                authProvider = authProvider
            )
        )
    }
}