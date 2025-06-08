package com.example.library.unit.auth.jwt

import com.example.library.auth.jwt.JwtConfiguration
import com.example.library.auth.jwt.JwtUtils
import com.example.library.user.UserEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.ZoneOffset

class JwtUtilsTest {

    private val jwtConfiguration: JwtConfiguration = JwtConfiguration().apply {
        secretKey = "nJf+3KztH8FQ1L0vX7f0ggamKKY8goJyh8u5Tlb6cFc="
        expiration = EXPIRATION_THRESHOLD
    }
    private var clock = Clock.fixed(Instant.parse("2021-05-07T11:10:09.123Z"), ZoneOffset.UTC)
    private val jwtUtils: JwtUtils = JwtUtils(jwtConfiguration, clock)

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
        clock = Clock.offset(clock, EXPIRATION_THRESHOLD.minus(Duration.ofNanos(1)))
        val validationResult = jwtUtils.isTokenValid(validJwt, userDetails)

        // then
        assertThat(validationResult).isTrue()
    }

    @Test
    fun `should NOT validate token is it's expired`() {
        // given
        val userDetails = getUserDetails()
        val validJwt = jwtUtils.generateToken(userDetails)

        // when
        clock = Clock.offset(clock, EXPIRATION_THRESHOLD)
        val validationResult = jwtUtils.isTokenValid(validJwt, userDetails)

        // then
        assertThat(validationResult).isTrue()
    }

    private fun getUserDetails() = UserEntity(1, "root", "password")

    private companion object {

        private val EXPIRATION_THRESHOLD = Duration.ofHours(1)
    }
}