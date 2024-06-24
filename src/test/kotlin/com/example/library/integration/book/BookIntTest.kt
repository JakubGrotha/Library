package com.example.library.integration.book

import com.example.library.book.BookDto
import com.example.library.book.BookEntity
import com.example.library.integration.AbstractIntegrationTest
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import kotlin.test.Test
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.*
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import java.time.LocalDate

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class BookIntTest : AbstractIntegrationTest() {

    @Test
    fun `should add book`() {
        // given
        val requestBody = getValidBookRequestBody()

        // when & then
        mockMvc.perform(
            post("/api/books")
                .contentType("application/json")
                .content(requestBody)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isCreated)

        val savedBooks = bookRepository.findAll()
        assertThat(savedBooks).hasSize(1)
        assertThat(savedBooks.first().title).isEqualTo("title")
        assertThat(savedBooks.first().author).isEqualTo("author")
        assertThat(savedBooks.first().isbn).isEqualTo("isbn")
        assertThat(savedBooks.first().publishedDate).isEqualTo(LocalDate.of(2020, 6, 1))
    }

    @ParameterizedTest
    @MethodSource("invalidBookRequests")
    fun `should NOT add book if request is incorrect`(requestBody: BookDto) {
        // given
        val invalidRequestBody = objectMapper.writeValueAsString(requestBody)

        // when & then
        mockMvc.perform(
            post("/api/books")
                .contentType("application/json")
                .content(invalidRequestBody)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `should get first book page with 10 elements if no request parameters are present`() {
        // given
        val books = getElevenBooks()
        books.forEach { bookRepository.save(BookEntity.fromRequest(it)) }
        books.removeLast()

        // when
        mockMvc.perform(
            get("/api/books")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()").value(10))
            .andExpectAll(
                MockMvcResultMatchers.jsonPath("$.content[0].title").value("title1"),
                MockMvcResultMatchers.jsonPath("$.content[1].title").value("title2"),
                MockMvcResultMatchers.jsonPath("$.content[2].title").value("title3"),
                MockMvcResultMatchers.jsonPath("$.content[3].title").value("title4"),
                MockMvcResultMatchers.jsonPath("$.content[4].title").value("title5"),
                MockMvcResultMatchers.jsonPath("$.content[5].title").value("title6"),
                MockMvcResultMatchers.jsonPath("$.content[6].title").value("title7"),
                MockMvcResultMatchers.jsonPath("$.content[7].title").value("title8"),
                MockMvcResultMatchers.jsonPath("$.content[8].title").value("title9"),
                MockMvcResultMatchers.jsonPath("$.content[9].title").value("title10")
            )
    }

    private fun getElevenBooks(): MutableList<BookDto> {
        return mutableListOf(
            BookDto("title1", "author1", "isbn1", LocalDate.of(1999, 1, 1)),
            BookDto("title2", "author1", "isbn2", LocalDate.of(1999, 1, 1)),
            BookDto("title3", "author1", "isbn3", LocalDate.of(1999, 1, 1)),
            BookDto("title4", "author1", "isbn4", LocalDate.of(1999, 1, 1)),
            BookDto("title5", "author1", "isbn5", LocalDate.of(1999, 1, 1)),
            BookDto("title6", "author1", "isbn6", LocalDate.of(1999, 1, 1)),
            BookDto("title7", "author1", "isbn7", LocalDate.of(1999, 1, 1)),
            BookDto("title8", "author1", "isbn8", LocalDate.of(1999, 1, 1)),
            BookDto("title9", "author1", "isbn9", LocalDate.of(1999, 1, 1)),
            BookDto("title10", "author1", "isbn10", LocalDate.of(1999, 1, 1)),
            BookDto("title11", "author1", "isbn11", LocalDate.of(1999, 1, 1)),
        )
    }

    @Test
    fun `should get book by id`() {
        // given
        val book = getBookEntity()
        val expected = bookRepository.save(book)

        // when & then
        mockMvc.perform(
            get("/api/books/${book.id}")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expected)))
    }

    @Test
    fun `should edit book`() {
        // given
        val entity = BookEntity(null, "old", "old", "old", LocalDate.of(2000, 1, 1))
        val oldBook = bookRepository.save(entity)
        val newBook = BookDto("new", "new", "new", LocalDate.of(2024, 12, 30))
        val expected = BookEntity(oldBook.id, newBook.title, newBook.author, newBook.isbn, newBook.publishedDate)

        // when & then
        mockMvc.perform(
            put("/api/books/${oldBook.id}")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(newBook))
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expected)))
    }

    @Test
    fun `should delete book by id`() {
        // given
        val book = bookRepository.save(getBookEntity())

        // when & then
        mockMvc.perform(
            delete("/api/books/${book.id}")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNoContent)
    }

    @ParameterizedTest
    @MethodSource("validBooks")
    fun `should find book by query`(validBook: BookEntity) {
        // given
        val randomBook = BookEntity(null, "random", "random", "random", LocalDate.of(2020, 1, 1))
        bookRepository.save(randomBook)
        bookRepository.save(validBook)
        val keyword = "keyword"

        // when & then
        mockMvc.perform(
            get("/api/books/search?query=${keyword}")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(listOf(validBook))))
    }

    @Test
    fun `should NOT edit book if it doesn't exist`() {
        // given
        val nonExistingBookId = 100
        val newValue = BookDto("keyword", "xyz", "xyz", LocalDate.of(2020, 1, 1))

        // when & then
        mockMvc.perform(
            put("/api/books/${nonExistingBookId}")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(newValue))
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(
                MockMvcResultMatchers.content().bytes("No book found with this id: $nonExistingBookId".toByteArray())
            )
    }

    private fun getValidBookRequestBody() = objectMapper.writeValueAsString(
        BookDto("title", "author", "isbn", LocalDate.of(2020, 6, 1))
    )

    private fun getBookEntity() = BookEntity(
        null, "title", "author", "isbn",
        LocalDate.of(1999, 1, 1)
    )

    companion object {

        private val VALID_DATE = LocalDate.of(2020, 1, 1)

        @JvmStatic
        private fun invalidBookRequests(): List<Arguments> {
            return listOf(
                arguments(BookDto("", "author", "isbn", VALID_DATE)),
                arguments(BookDto(" ", "author", "isbn", VALID_DATE)),
                arguments(BookDto("title", "", "isbn", VALID_DATE)),
                arguments(BookDto("title", " ", "isbn", VALID_DATE)),
                arguments(BookDto("title", "author", "", VALID_DATE)),
                arguments(BookDto("title", "author", " ", VALID_DATE)),
            )
        }

        @JvmStatic
        private fun validBooks(): List<Arguments> {
            return listOf(
                arguments(BookEntity(null, "keyword", "xyz", "xyz", VALID_DATE)),
                arguments(BookEntity(null, "xyz", "keyword", "xyz", VALID_DATE)),
                arguments(BookEntity(null, "xyz", "xyz", "keyword", VALID_DATE)),
                arguments(BookEntity(null, "keywordxyz", "xyz", "xyz", VALID_DATE)),
                arguments(BookEntity(null, "xyz", "xyzkeywordxyz", "xyz", VALID_DATE)),
                arguments(BookEntity(null, "xyz", "xyz", "xyzkeyword", VALID_DATE)),
            )
        }
    }
}