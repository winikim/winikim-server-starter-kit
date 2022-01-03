package me.winikim

import me.winikim.adminuser.domain.entity.AdminUser
import me.winikim.adminuser.domain.enums.AdminUserStatus
import me.winikim.adminuser.infrastructure.repository.AdminUserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.annotation.PostConstruct

@SpringBootApplication
class WinikimServerStarterApplication {
    @Value("\${admin.default.username}")
    lateinit var adminUsername: String
    @Value("\${admin.default.password}")
    lateinit var adminPassword: String
    @Autowired
    lateinit var adminUserRepository: AdminUserRepository
    @Autowired
    lateinit var bCryptPasswordEncoder: BCryptPasswordEncoder
    @PostConstruct
    fun init() {
        // default admin 생성
        println("postConstruct")
        adminUserRepository.save(
            AdminUser(
                username = adminUsername,
                password = bCryptPasswordEncoder.encode(adminPassword),
                status = AdminUserStatus.ACTIVE
            )
        )
    }
}
fun main(args: Array<String>) {
    runApplication<WinikimServerStarterApplication>(*args)
}