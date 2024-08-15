package com.example.library.auth

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class AuthenticationController(
    private val authenticationService: AuthenticationService
) {

    @PostMapping("/register")
    fun login(@Valid @RequestBody request: AuthenticationRequest): ResponseEntity<AuthenticationResponse> {
        val authenticationResult = authenticationService.register(request)
        return ResponseEntity.ok(authenticationResult)
    }

    @PostMapping("/login")
    fun register(@Valid @RequestBody request: AuthenticationRequest): ResponseEntity<AuthenticationResponse> {
        val authenticationResult = authenticationService.authenticate(request)
        return ResponseEntity.ok(authenticationResult)
    }
}
