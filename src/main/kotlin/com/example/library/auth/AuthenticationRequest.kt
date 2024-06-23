package com.example.library.auth

import jakarta.validation.constraints.NotBlank

data class AuthenticationRequest(

    @NotBlank
    val username: String,

    @NotBlank
    val password: String
)
