package com.example.library.book

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

data class BookDto(

    @field:NotBlank(message = "Title must not be null, empty or blank")
    val title: String,

    @field:NotBlank(message = "Author must not be null, empty or blank")
    val author: String,

    @field:NotBlank(message = "Isbn must not be null, empty or blank")
    val isbn: String,

    @field:NotNull(message = "published date must not be null")
    @field:DateTimeFormat(pattern = "yyyy-MM-dd")
    val publishedDate: LocalDate
)
