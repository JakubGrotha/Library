package com.example.library.book

import org.springframework.stereotype.Service

@Service
class BookService(
    private val bookRepository: BookRepository,
) {
    fun addNewBook(request: BookDto): BookEntity {
        val bookEntity = BookEntity.fromRequest(request)
        return bookRepository.save(bookEntity)
    }

    fun getAllBooks(): List<BookEntity> {
        return bookRepository.findAll().toList()
    }

    fun getBookById(bookId: Long): BookEntity {
        return bookRepository.findById(bookId).orElseThrow()
    }

    fun editBook(bookId: Long, request: BookDto): BookEntity {
        if (!bookRepository.existsById(bookId)) {
            throw BookNotFoundException("No book found with this id: $bookId")
        }
        val bookEntity = BookEntity.fromRequest(request)
        bookEntity.id = bookId
        return bookRepository.save(bookEntity)
    }

    fun searchBooks(query: String): List<BookEntity> {
        return bookRepository.searchBooks(query).toList()
    }

    fun deleteBookById(bookId: Long) {
        return bookRepository.deleteById(bookId)
    }
}