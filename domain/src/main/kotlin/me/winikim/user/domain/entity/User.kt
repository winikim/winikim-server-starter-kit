package me.winikim.user.domain.entity

import me.winikim.user.domain.enums.UserStatus
import me.winikim.user.domain.enums.AuthProvider
import javax.persistence.*

@Entity
class User(
    authProviderId: String,
    authProvider: AuthProvider,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(unique = true)
    var username: String = authProvider.name[0] + authProviderId

    var authProvider: AuthProvider = authProvider

    var status: UserStatus = UserStatus.REQUIRED_SIGN_UP

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var userAuthorities: MutableList<UserAuthority> = mutableListOf()

}