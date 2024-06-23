package com.example.library.book

import jakarta.validation.ConstraintViolation

class InvalidBookRequestException(
    violations: Set<ConstraintViolation<BookDto>>
) : RuntimeException(
    violations.joinToString { it.message }
)