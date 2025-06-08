package com.example.library.book

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class BookRepository(private val jdbcClient: JdbcClient) {

    fun save(bookEntity: BookEntity): BookEntity {
        val sql = """
            INSERT INTO books(title, author, isbn, published_at)
            VALUES (:title, :author, :isbn, :published_at)
            RETURNING id
        """.trimIndent()
        val params = mapOf(
            "title" to bookEntity.title,
            "author" to bookEntity.author,
            "isbn" to bookEntity.isbn,
            "published_at" to bookEntity.publishedAt
        )
        val id = jdbcClient.sql(sql)
            .params(params)
            .query(Long::class.java)
            .single()
        return bookEntity.copy(id = id)
    }

    fun updateBook(bookEntity: BookEntity): BookEntity {
        val sql = """
            UPDATE books
            SET title = :title, author = :author, isbn = :isbn, published_at = :published_at
            WHERE id = :id
        """.trimIndent()
        val params = mapOf(
            "title" to bookEntity.title,
            "author" to bookEntity.author,
            "isbn" to bookEntity.isbn,
            "published_at" to bookEntity.publishedAt,
            "id" to bookEntity.id
        )
        jdbcClient.sql(sql)
            .params(params)
            .update()
        return bookEntity
    }

    fun findAll(pageable: Pageable): Page<BookEntity> {
        val sql = """
            SELECT id, title, author, isbn, published_at
            FROM books
            LIMIT :limit
            OFFSET :offset
        """.trimIndent()
        val params = mapOf(
            "limit" to pageable.pageSize,
            "offset" to pageable.pageNumber * pageable.pageSize
        )
        val result =  jdbcClient.sql(sql)
            .params(params)
            .query(BookEntity::class.java)
            .list()
        return PageImpl(result)
    }

    fun findById(id: Long): Optional<BookEntity> {
        val sql = """
            SELECT * FROM books
            WHERE id = :id
        """.trimIndent()
        return jdbcClient.sql(sql)
            .param("id", id)
            .query(BookEntity::class.java)
            .optional()
    }

    fun existsById(id: Long): Boolean {
        val sql = """
            SELECT COUNT(*) FROM BOOKS
            WHERE id = :id
        """.trimIndent()
        val count = jdbcClient.sql(sql)
            .param("id", id)
            .query(Int::class.java)
            .single()
        return count == 1
    }

    fun deleteById(id: Long) {
        val sql = """
            DELETE FROM books
            WHERE id = :id
        """.trimIndent()
        val rowsAffected = jdbcClient.sql(sql)
            .param("id", id)
            .update()
        if (rowsAffected > 1) throw RuntimeException("Removed more than 1 row. Rollback")
    }

    fun searchBooks(query: String, pageable: Pageable): Page<BookEntity> {
        val sql = """
            SELECT * FROM books
            WHERE title LIKE CONCAT('%', :query, '%')
            OR author LIKE CONCAT('%', :query, '%')
            OR isbn LIKE CONCAT('%', :query, '%')    
            LIMIT :limit
            OFFSET :offset
        """.trimIndent()
        val params = mapOf(
            "query" to query,
            "limit" to pageable.pageSize,
            "offset" to pageable.pageNumber * pageable.pageSize
        )
        val result = jdbcClient.sql(sql)
            .params(params)
            .query(BookEntity::class.java)
            .list()
        return PageImpl(result)
    }

    fun findBookContainingKeyword(keyword: String, pageable: Pageable): Page<BookEntity> {
        val sql = """
            SELECT * FROM books
            WHERE full_text_search @@ to_tsquery(:keyword)
            LIMIT :limit
            OFFSET :offset
            """.trimIndent()
        val params = mapOf(
            "keyword" to keyword,
            "limit" to pageable.pageSize,
            "offset" to pageable.pageNumber * pageable.pageSize
        )
        val result = jdbcClient.sql(sql)
            .params(params)
            .query(BookEntity::class.java)
            .list()
        return PageImpl(result)
    }
}
