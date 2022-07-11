package me.winikim.user.presentation.web

import me.winikim.user.application.service.UserService
import me.winikim.user.application.dto.SignInRequest
import me.winikim.user.application.dto.SignInResponse
import me.winikim.user.domain.enums.AuthProvider
import org.apache.logging.log4j.LogManager.getLogger
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import javax.servlet.http.HttpServletRequest

@RestController
class SignController(
    private val userService: UserService,
    @Value("\${kakao.redirect-uri}") private val redirectUri: String,
    @Value("\${kakao.client-id}") private val clientId: String
) {
    private val log = getLogger()

    @PostMapping("sign-in")
    fun signIn(@RequestBody signInRequest: SignInRequest) {
        userService.signIn(signInRequest)
    }

    @GetMapping("kakao-sign-in")
    fun redirectTokakaoSignPageForTest(): ResponseEntity<*> {
        val redirectUrl = "https://kauth.kakao.com/oauth/authorize?client_id=${clientId}&redirect_uri=${redirectUri}&response_type=code"
        val uri = URI(redirectUrl)
        val headers = HttpHeaders()
        headers.location = uri
        return ResponseEntity<Any>(headers, HttpStatus.SEE_OTHER)
    }

    @GetMapping("kakao-sign-in/callback")
    fun kakaoSigninCallback(request: HttpServletRequest, @RequestParam("code") code: String): SignInResponse {
        return userService.signIn(SignInRequest(AuthProvider.KAKAO, code))
    }
}
