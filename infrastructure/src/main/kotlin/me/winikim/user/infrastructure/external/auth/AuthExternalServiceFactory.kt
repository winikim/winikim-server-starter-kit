package me.winikim.user.infrastructure.external.auth

import me.winikim.user.domain.enums.AuthProvider
import org.springframework.stereotype.Component
import org.springframework.util.CollectionUtils

@Component
class AuthExternalServiceFactory {
    private val authExternalServices: MutableMap<AuthProvider, AuthExternalService> = mutableMapOf()

    constructor(authExternalServiceList: List<AuthExternalService>) {
        if (CollectionUtils.isEmpty(authExternalServiceList)) {
            throw IllegalArgumentException("AuthExternalService 구현체 없음")
        }

        authExternalServiceList.map { this.authExternalServices.put(it.getAuthProvider(), it) }
    }

    fun getService(authProvider: AuthProvider): AuthExternalService {
        return authExternalServices[authProvider]!!
    }
}