package me.winikim.security.presentation

import me.winikim.security.application.service.JwtTokenProvider
import me.winikim.security.application.service.JwtUser
import me.winikim.share.doamin.exception.InvalidIssuerException
import me.winikim.share.doamin.exception.TokenExpiredException
import me.winikim.user.domain.enums.UserStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthenticationTokenFilter(
    private val jwtTokenProvider: JwtTokenProvider,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        val requestHeader: String? = request.getHeader("Authorization")
        if (requestHeader != null) {
            val authToken = requestHeader.substring(7)
            val claims = jwtTokenProvider.getClaims(authToken)
            val userId = claims.subject
            val expiration = claims.expiration
            val authorities = claims["authorities"] as List<String>
            val status = UserStatus.valueOf(claims["status"].toString())
            val issuer = claims.issuer
            if (jwtTokenProvider.isNotIssuer(issuer)) throw InvalidIssuerException()
            if (jwtTokenProvider.isTokenExpired(expiration)) throw TokenExpiredException()

//            val userDetails = userId?.let { userDetailsService.loadUserByUsername(userId) }
            val jwtUser = JwtUser(
                id = userId.toLong(),
                authorities = authorities.map { SimpleGrantedAuthority(it) }.toMutableList(),
                status = status
            )

            SecurityContextHolder.getContext().authentication =
                UsernamePasswordAuthenticationToken(jwtUser, jwtUser, jwtUser.authorities)
        }
        filterChain.doFilter(request, response)
    }
}