package com.example.library.integration

import com.example.library.book.BookRepository
import com.example.library.user.UserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc

@SpringBootTest
@AutoConfigureMockMvc
abstract class AbstractIntegrationTest {

    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    @Autowired
    protected lateinit var bookRepository: BookRepository

    @Autowired
    protected lateinit var userRepository: UserRepository

    @BeforeEach
    protected fun setup() {
        cleanAllTables()
    }

    private fun cleanAllTables() {
        bookRepository.deleteAll()
        userRepository.deleteAll()
    }
}