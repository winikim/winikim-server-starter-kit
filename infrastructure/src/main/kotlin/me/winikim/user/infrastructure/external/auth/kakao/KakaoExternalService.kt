package me.winikim.user.infrastructure.external.auth.kakao

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import me.winikim.user.domain.enums.AuthProvider
import me.winikim.user.infrastructure.external.auth.AuthExternalService
import me.winikim.user.infrastructure.external.auth.dto.TokenOutput
import me.winikim.user.infrastructure.external.auth.dto.UserInfoOutput
import me.winikim.user.infrastructure.external.auth.kakao.dto.KakaoTokenResponse
import me.winikim.user.infrastructure.external.auth.kakao.dto.KakaoUserInfoResponse
import org.apache.logging.log4j.LogManager.getLogger
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.net.URL

@Component
class KakaoExternalService(
    @Value("\${kakao.token-uri}") private val tokenUri: String,
    @Value("\${kakao.user-info-uri}") private val userInfoUri: String,
    @Value("\${kakao.client-id}") private val clientId: String,
    @Value("\${kakao.client-secret}") private val clientSecret: String,
    @Value("\${kakao.redirect-uri}") private val redirectUri: String
) : AuthExternalService {
    private val log = getLogger()
    private val tokenEndpoint: URL = URL(tokenUri)
    private val userInfoEndpoint: URL = URL(userInfoUri)
    private val httpClient = HttpClient(
        engineFactory = Apache
    ) {
//        install(HttpTimeout) {
//
//        }
        install(JsonFeature) {
            serializer =
                JacksonSerializer(
                    jacksonObjectMapper().enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                )
        }
    }

    override fun getToken(code: String): TokenOutput = runBlocking {

        val response = httpClient.submitForm<KakaoTokenResponse>(
            url = "$tokenEndpoint",
            formParameters = Parameters.build {
                append("grant_type", "authorization_code")
                append("client_id", clientId)
                append("redirect_uri", redirectUri)
                append("code", code)
                append("client_secret", clientSecret)
            },
            encodeInQuery = false
        )
        log.debug(response.toString())
        log.debug(response.access_token)

        return@runBlocking TokenOutput(
            tokenType = response.token_type,
            accessToken = response.access_token,
            expiresIn = response.expires_in,
            refreshToken = response.refresh_token,
            refreshTokenExpiresIn = response.refresh_token_expires_in
        )
    }

    override fun getUserInfo(accessToken: String): UserInfoOutput = runBlocking {
        log.debug("accessToken :" + accessToken)
        val response = httpClient.submitForm<KakaoUserInfoResponse> {
            url(userInfoEndpoint)
            headers {
                header("Authorization", "Bearer $accessToken")
            }
        }
        return@runBlocking UserInfoOutput(response.id.toString())
    }

    override fun getAuthProvider(): AuthProvider = AuthProvider.KAKAO
}