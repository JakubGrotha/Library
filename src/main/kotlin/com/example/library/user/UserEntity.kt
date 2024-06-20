package com.example.library.user

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
data class UserEntity(

    @Id
    @GeneratedValue
    val id: Long?,
    @get:JvmName("username")
    val username: String,
    @get:JvmName("password")
    val password: String
) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf()

    override fun getUsername(): String = username

    override fun getPassword(): String = password
}
