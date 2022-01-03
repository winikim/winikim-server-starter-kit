package me.winikim.user.domain.entity

import javax.persistence.*

@Entity
class UserAuthority(
    user: User,
    authority: Authority
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = ForeignKey(name = "FK_USER_ID"))
    val user: User = user

    @ManyToOne
    @JoinColumn(name = "authority_id", foreignKey = ForeignKey(name = "FK_AUTHORITY_ID"))
    val authority: Authority = authority


}