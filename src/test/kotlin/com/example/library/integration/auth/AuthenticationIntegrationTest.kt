package com.example.library.integration.auth

import com.example.library.auth.AuthenticationRequest
import com.example.library.integration.AbstractIntegrationTest
import com.example.library.user.UserEntity
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

class AuthenticationIntegrationTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Test
    fun `should register user`() {
        // given
        val validAuthenticationRequest = AuthenticationRequest("username", "password")

        // when & then
        mockMvc.perform(
            post("/api/users/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(validAuthenticationRequest))
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `registration should fail if user already exists with the same username`() {
        // given
        val user = UserEntity(null, "username", "password")
        userRepository.save(user)
        val invalidAuthenticationRequest = AuthenticationRequest("username", "pass")

        // when
        mockMvc.perform(
            post("/api/users/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(objectMapper.writeValueAsString(invalidAuthenticationRequest)))
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest)

    }

    @Test
    fun `should authenticate user`() {
        // given
        val existingUser = UserEntity(null, "username", passwordEncoder.encode("password"))
        userRepository.save(existingUser)
        val validAuthenticationRequest = AuthenticationRequest("username", "password")

        // when & then
        mockMvc.perform(
            post("/api/users/login")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(validAuthenticationRequest))
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
    }
}