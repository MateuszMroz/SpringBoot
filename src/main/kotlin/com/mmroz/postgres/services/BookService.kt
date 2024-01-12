package com.mmroz.postgres.services

import com.mmroz.postgres.domain.entities.BookEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface BookService {
    fun createUpdateBook(isbn: String, bookEntity: BookEntity): BookEntity?
    fun findAll(): List<BookEntity>
    fun findAll(pageable: Pageable): Page<BookEntity>
    fun findBookById(isbn: String): BookEntity?
    fun partialUpdate(isbn: String, bookEntity: BookEntity): BookEntity?
    fun delete(isbn: String)
    fun isExist(isbn: String): Boolean
}