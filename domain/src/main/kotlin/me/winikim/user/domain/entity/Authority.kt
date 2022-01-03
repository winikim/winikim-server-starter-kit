package me.winikim.user.domain.entity

import me.winikim.user.domain.enums.AuthorityName
import javax.persistence.*

@Entity
class Authority(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    val authorityName: AuthorityName,

    @OneToMany(mappedBy = "authority", fetch = FetchType.LAZY)
    var userAuthorities: MutableList<UserAuthority> = mutableListOf()
)