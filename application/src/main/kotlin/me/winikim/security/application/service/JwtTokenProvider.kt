package me.winikim.security.application.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwt
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import java.util.function.Function


@Component
class JwtTokenProvider(
    @Value("\${jwt.secret}") private val secretKey: String,
    @Value("\${jwt.expiration}") private val expiration: Long,
) {
    private val tokenIssuer = "winikim"

    private fun generateAudience(): String {
        return "mobile"
    }

    fun generateToken(jwtUser: JwtUser): String {
        val claims = mutableMapOf<String, Any>()
        claims["id"] = jwtUser.id
        claims["authorities"] = jwtUser.authorities.map { it.authority }
        claims["status"] = jwtUser.status
        return doGenerateToken(claims, jwtUser.id.toString(), generateAudience())
    }

    private fun doGenerateToken(
        claims: Map<String, Any>,
        subject: String,
        audience: String
    ): String {
        val now = Date()
        val expirationDate = Date(now.time + expiration * 1000)

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setAudience(audience)
            .setIssuedAt(now)
            .setIssuer(tokenIssuer)
            .setExpiration(expirationDate)
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .compact()
    }

    fun getClaims(token: String): Claims {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body
    }

    fun isNotIssuer(issuer: String): Boolean {
        return issuer != tokenIssuer
    }

    fun isTokenExpired(expiredDate: Date): Boolean {
        // 자동 로그인일 경우 token 만료 시간 보다 3개월이 지난 경우 다시 로그인 시킨다.
        return expiredDate.before(Date())
    }
}
