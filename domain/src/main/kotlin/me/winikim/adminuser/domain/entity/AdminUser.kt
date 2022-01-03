package me.winikim.adminuser.domain.entity

import me.winikim.adminuser.domain.enums.AdminUserStatus
import me.winikim.user.domain.enums.AdminAuthorityName
import javax.persistence.*

@Entity
class AdminUser(
    username: String,
    password: String,
    status: AdminUserStatus
) {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    val id: Int = 0

    @Column(name = "username", unique = true)
    val username: String = username

    @Column(name = "password")
    var password: String = password
        protected set

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    var status: AdminUserStatus = status
        protected set

    @Enumerated(EnumType.STRING)
    @Column(name = "authority")
    var authorityName: AdminAuthorityName = AdminAuthorityName.ROLE_ADMIN
        protected set

    fun changePassword(encryptedPassword: String) {
        this.password = encryptedPassword
    }

    fun approve() {
        this.status = AdminUserStatus.ACTIVE
    }

}