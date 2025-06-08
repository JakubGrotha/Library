package com.example.library.book

import jakarta.validation.Validator
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class BookService(
    private val bookRepository: BookRepository,
    private val validator: Validator
) {

    fun addNewBook(request: BookDto): BookEntity {
        val violations = validator.validate(request)
        if (violations.isNotEmpty()) {
            throw InvalidBookRequestException(violations)
        }
        val bookEntity = BookEntity.fromRequest(request)
        return bookRepository.save(bookEntity)
    }

    fun getAllBooks(pageable: Pageable): Page<BookEntity> {
        return bookRepository.findAll(pageable)
    }

    fun getBookById(bookId: Long): BookEntity {
        return bookRepository.findById(bookId).orElseThrow()
    }

    fun editBook(bookId: Long, request: BookDto): BookEntity {
        if (!bookRepository.existsById(bookId)) {
            throw BookNotFoundException("No book found with this id: $bookId")
        }
        val bookEntity = BookEntity.fromRequest(request)
        val updatedBookEntity = bookEntity.copy(id = bookId)
        return bookRepository.updateBook(updatedBookEntity)
    }

    fun searchBooks(query: String, pageable: Pageable): List<BookEntity> {
        return bookRepository.searchBooks(query, pageable).toList()
    }

    fun deleteBookById(bookId: Long) {
        return bookRepository.deleteById(bookId)
    }

    fun getBooksContainingKeyword(keyword: String, pageable: Pageable): Page<BookEntity> {
        return bookRepository.findBookContainingKeyword(keyword, pageable)

    }
}
