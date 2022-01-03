package me.winikim.security.presentation

import me.winikim.adminuser.domain.enums.AdminUserStatus
import me.winikim.security.application.service.AdminJwtTokenProvider
import me.winikim.security.application.service.JwtAdminUser
import me.winikim.share.doamin.exception.InvalidIssuerException
import me.winikim.share.doamin.exception.TokenExpiredException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AdminJwtAuthenticationTokenFilter(
    private val adminJwtTokenProvider: AdminJwtTokenProvider
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val tokenHeader: String? = request.getHeader("X-ADMIN-TOKEN")
        if (tokenHeader != null) {
            val authToken = tokenHeader.substring(7)
            val claims = adminJwtTokenProvider.getClaims(authToken)
            val userId = claims.subject
            val expiration = claims.expiration
            val authorities = claims["authorities"] as List<String>
            val username = claims["username"] as String
            val status = AdminUserStatus.valueOf(claims["status"].toString())
            val issuer = claims.issuer
            if (adminJwtTokenProvider.isNotIssuer(issuer)) throw InvalidIssuerException()
            if (adminJwtTokenProvider.isTokenExpired(expiration)) throw TokenExpiredException()

            val jwtUser = JwtAdminUser(
                id = userId.toInt(),
                authorities = authorities.map { SimpleGrantedAuthority(it) }.toMutableList(),
                status = status,
                username = username
            )

            SecurityContextHolder.getContext().authentication =
                UsernamePasswordAuthenticationToken(jwtUser, jwtUser, jwtUser.authorities)
        }
        filterChain.doFilter(request, response)
    }
}