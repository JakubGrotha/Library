package com.example.library.book

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/books")
class BookController(
    private val bookService: BookService
) {

    @PostMapping
    fun addBook(@RequestBody request: BookDto): ResponseEntity<BookEntity> {
        val createdBook = bookService.addNewBook(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook)
    }

    @GetMapping
    fun getBookPage(
        @PageableDefault(page = 0, size = 10, sort = ["title", "author", "isbn"]) pageable: Pageable,
    ): Page<BookEntity> {
        return bookService.getAllBooks(pageable)
    }

    @GetMapping("/{id}")
    fun getBook(@PathVariable("id") bookId: Long): BookEntity {
        return bookService.getBookById(bookId)
    }

    @PutMapping("/{id}")
    fun editBook(@PathVariable("id") bookId: Long, @RequestBody request: BookDto): BookEntity {
        return bookService.editBook(bookId, request)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    fun deleteBook(@PathVariable("id") bookId: Long) {
        bookService.deleteBookById(bookId)
    }

    @GetMapping("/search")
    fun searchBooks(
        @RequestParam("query") query: String,
        @PageableDefault(page = 0, size = 10, sort = ["title", "author", "isbn"]) pageable: Pageable,
    ): List<BookEntity> {
        return bookService.searchBooks(query, pageable)
    }
}
