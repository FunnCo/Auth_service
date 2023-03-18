package com.crowtest.auth_service.entity

import com.crowtest.auth_service.entity.enum.Role
import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

@Entity
@Table(name = "user", schema = "public")
class User(
        @Id
        @GeneratedValue(generator = "UUID")
        @GenericGenerator(
                name = "UUID",
                strategy = "org.hibernate.id.UUIDGenerator"
        )
        @Column(name = "user_id")
        val userId: UUID?,

        @ManyToOne
        @JoinColumn(name = "group_id")
        var group: Group,

        @Enumerated(EnumType.STRING)
        @Column(name = "role")
        val role: Role,

        @Column(name = "firstname")
        var firstname: String,

        @Column(name = "lastname")
        var lastname: String,

        @Column(name = "patronymic")
        var patronymic: String?,

        @Column(name = "email", unique = true)
        var email: String,

        @Column(name = "password", unique = true)
        var userPassword: String,

        @Column(name = "phone")
        var phone: String
) : UserDetails {
        override fun toString(): String {
                return "User(userId=$userId, group=$group, firstname='$firstname', lastname='$lastname', patronymic='$patronymic', email='$email', password='$userPassword', phone='$phone')"
        }

        override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
                return mutableListOf(SimpleGrantedAuthority(role.name))
        }

        override fun getPassword(): String {
                return userPassword
        }

        override fun getUsername(): String {
                return email
        }

        override fun isAccountNonExpired(): Boolean {
                return true
        }

        override fun isAccountNonLocked(): Boolean {
                return true
        }

        override fun isCredentialsNonExpired(): Boolean {
                return true
        }

        override fun isEnabled(): Boolean {
                return true
        }
}