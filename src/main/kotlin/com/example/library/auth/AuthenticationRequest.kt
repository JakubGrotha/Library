package com.example.library.auth

import jakarta.validation.constraints.NotBlank

data class AuthenticationRequest(

    @field:NotBlank
    val username: String,

    @field:NotBlank
    val password: String
)
