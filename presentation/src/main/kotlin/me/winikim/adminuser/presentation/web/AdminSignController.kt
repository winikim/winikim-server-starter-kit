package me.winikim.adminuser.presentation.web

import me.winikim.adminuser.application.dto.AdminSignInRequest
import me.winikim.adminuser.application.dto.AdminSignInResponse
import me.winikim.adminuser.application.dto.AdminUserMeResponse
import me.winikim.adminuser.application.dto.CreateAdminUserRequest
import me.winikim.adminuser.application.service.AdminUserService
import me.winikim.security.application.service.JwtAdminUser
import net.yayoung.share.application.dto.PageResponse
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
class AdminSignController(
    private val adminUserService: AdminUserService
) {

    @PostMapping("admin/sign-in")
    fun signIn(@RequestBody adminSignInRequest: AdminSignInRequest): AdminSignInResponse {
        return adminUserService.signIn(adminSignInRequest)
    }

    @PostMapping("admin/users")
    @PreAuthorize("isAuthenticated()")
    fun createAdminUser(@RequestBody createAdminUserRequest: CreateAdminUserRequest) {
        adminUserService.createAdminUser(createAdminUserRequest)
    }

    @GetMapping("admin/user/me")
    fun adminUserMe(@AuthenticationPrincipal jwtAdminUser: JwtAdminUser): AdminUserMeResponse {
        return AdminUserMeResponse(jwtAdminUser)
    }

    @GetMapping("admin/users")
    @PreAuthorize("isAuthenticated()")
    fun retrieveAdminUsers(pageable: Pageable): PageResponse {
        return adminUserService.retrieveAdminUsers(pageable)
    }

    @DeleteMapping("admin/users/{adminUserId}")
    @PreAuthorize("isAuthenticated()")
    fun deleteAdminUser(@PathVariable adminUserId: Int) {
        adminUserService.deleteAdminUser(adminUserId)
    }
}