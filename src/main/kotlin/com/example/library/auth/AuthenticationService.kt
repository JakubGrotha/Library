package com.example.library.auth

import com.example.library.auth.jwt.JwtUtils
import com.example.library.user.UserService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val authenticationManager: AuthenticationManager,
    private val userService: UserService,
    private val jwtUtils: JwtUtils
) {

    fun authenticate(request: AuthenticationRequest): AuthenticationResponse {
        val username = request.username
        val password = request.password
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(username, password)
        )
        SecurityContextHolder.getContext().authentication = authentication
        val appUser = userService.loadUserByUsername(username)
        val token = jwtUtils.generateToken(appUser)
        return AuthenticationResponse(token)
    }

    fun register(request: AuthenticationRequest): AuthenticationResponse {
        val user = userService.registerUser(request)
        val token = jwtUtils.generateToken(user)
        return AuthenticationResponse(token)
    }
}
