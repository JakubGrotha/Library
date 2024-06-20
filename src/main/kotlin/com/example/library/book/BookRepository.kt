package com.example.library.book

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface BookRepository : CrudRepository<BookEntity, Long> {

    @Query(
        """
    SELECT * FROM books
    WHERE title LIKE CONCAT('%', :query, '%')
    OR author LIKE CONCAT('%', :query, '%')
    OR isbn LIKE CONCAT('%', :query, '%')
    ;
    """, nativeQuery = true
    )
    fun searchBooks(@Param("query") query: String): Iterable<BookEntity>
}
