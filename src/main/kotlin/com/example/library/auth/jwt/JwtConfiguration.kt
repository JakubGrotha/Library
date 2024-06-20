package com.example.library.auth.jwt

import org.jetbrains.annotations.NotNull
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.validation.annotation.Validated
import java.time.Duration

@Configuration
@ConfigurationProperties("jwt")
@Validated
class JwtConfiguration {

    @NotNull
    lateinit var secretKey: String

    @NotNull
    lateinit var expiration: Duration

}