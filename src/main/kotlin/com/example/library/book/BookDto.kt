package com.example.library.book

import java.time.LocalDate

data class BookDto(
    val title: String,
    val author: String,
    val isbn: String,
    val publishedDate: LocalDate
)
