package com.example.library.book

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "books")
data class BookEntity(
    @Id
    @GeneratedValue
    var id: Long?,
    val title: String,
    val author: String,
    val isbn: String,
    val publishedDate: LocalDate
) {

    companion object {

        fun fromRequest(request: BookDto): BookEntity =
            BookEntity(
                id = null,
                title = request.title,
                author = request.author,
                isbn = request.isbn,
                publishedDate = request.publishedDate
            )
    }
}
