package me.winikim.user.presentation.web

import me.winikim.security.application.service.JwtUser
import me.winikim.user.application.dto.UserMeResponse
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {

    @GetMapping("user/me")
    fun userMe(@AuthenticationPrincipal jwtUser: JwtUser): UserMeResponse {
        return UserMeResponse(jwtUser)
    }
}