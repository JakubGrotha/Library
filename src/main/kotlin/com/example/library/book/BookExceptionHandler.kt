package com.example.library.book

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class BookExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [BookNotFoundException::class, InvalidBookRequestException::class])
    fun handleException(ex: RuntimeException, request: WebRequest): ResponseEntity<Any>? {
        val responseBody = ex.message
        return handleExceptionInternal(ex, responseBody, HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }
}