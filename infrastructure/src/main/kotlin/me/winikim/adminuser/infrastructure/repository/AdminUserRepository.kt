package me.winikim.adminuser.infrastructure.repository

import me.winikim.adminuser.domain.entity.AdminUser
import org.springframework.data.jpa.repository.JpaRepository

interface AdminUserRepository : JpaRepository<AdminUser, Int> {
    fun findByUsername(username: String): AdminUser?
    fun existsByUsername(username: String): Boolean
}