package com.example.library.user

import com.example.library.auth.AuthenticationRequest
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
            ?: throw UserNotFoundException("No user found with username: $username")
        return User.builder()
            .username(user.username)
            .password(user.password)
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(false)
            .build()
    }

    fun registerUser(request: AuthenticationRequest): UserEntity {
        checkIfUserAlreadyExists(request)
        val userEntity = UserEntity(
            id = null,
            username = request.username,
            password = passwordEncoder.encode(request.password)
        )
        userRepository.save(userEntity)
        return userEntity
    }

    private fun checkIfUserAlreadyExists(request: AuthenticationRequest) {
        if (userRepository.findByUsername(request.username) != null) {
            throw UserAlreadyExistsException("User already exists with this username: ${request.username}")
        }
    }
}