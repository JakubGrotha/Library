package com.example.library.unit.auth.jwt

import com.example.library.auth.jwt.JwtConfiguration
import com.example.library.auth.jwt.JwtUtils
import com.example.library.user.UserEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Duration

class JwtUtilsTest {

    private val jwtConfiguration: JwtConfiguration = JwtConfiguration().apply {
        secretKey = "nJf+3KztH8FQ1L0vX7f0ggamKKY8goJyh8u5Tlb6cFc="
        expiration = Duration.ofHours(1)
    }
    private val jwtUtils: JwtUtils = JwtUtils(jwtConfiguration)

    @Test
    fun `should extract username`() {
        // given
        val userDetails = getUserDetails()
        val validJwt = jwtUtils.generateToken(userDetails)

        // when
        val actual = jwtUtils.extractUsername(validJwt)

        // then
        assertThat(actual).isEqualTo("root")
    }

    @Test
    fun `should validate token`() {
        // given
        val userDetails = getUserDetails()
        val validJwt = jwtUtils.generateToken(userDetails)

        // when
        val validationResult = jwtUtils.isTokenValid(validJwt, userDetails)

        // then
        assertThat(validationResult).isTrue()
    }

    private fun getUserDetails() = UserEntity(1, "root", "password")
}