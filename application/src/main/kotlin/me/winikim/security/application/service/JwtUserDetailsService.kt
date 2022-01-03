package me.winikim.security.application.service

import me.winikim.share.doamin.exception.NotFoundUserException
import me.winikim.user.domain.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service(value = "userDetailsService")
class JwtUserDetailsService(
        private val userRepository: UserRepository
): UserDetailsService {

    override fun loadUserByUsername(userId: String): UserDetails {

        val user = userRepository.findByIdOrNull(userId.toLong()) ?: throw NotFoundUserException()

        return JwtUser(
            id = user.id,
            authorities = user.userAuthorities.map { SimpleGrantedAuthority(it.authority.authorityName.name) }.toMutableList(),
            status = user.status,
        )
    }
}