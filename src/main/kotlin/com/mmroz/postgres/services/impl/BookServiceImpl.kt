package com.mmroz.postgres.services.impl

import com.mmroz.postgres.domain.entities.BookEntity
import com.mmroz.postgres.repositories.BookRepository
import com.mmroz.postgres.services.BookService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class BookServiceImpl(private val repository: BookRepository) : BookService {

    override fun createUpdateBook(isbn: String, bookEntity: BookEntity): BookEntity? {
        return if (isbn == bookEntity.isbn) {
            repository.save(bookEntity)
        } else null
    }

    override fun findAll(): List<BookEntity> = repository.findAll().toList()

    override fun findAll(pageable: Pageable): Page<BookEntity> = repository.findAll(pageable)

    override fun findBookById(isbn: String): BookEntity? = repository.findById(isbn).getOrNull()

    override fun partialUpdate(isbn: String, bookEntity: BookEntity): BookEntity? {
        return if (isbn == bookEntity.isbn) {
            val original = repository.findById(isbn).getOrNull()
                    ?: throw NoSuchElementException("Could not find author with accountId")

            repository.save(original.copy(
                    title = bookEntity.title ?: original.title,
                    authorEntity = bookEntity.authorEntity ?: original.authorEntity
            ))
        } else null
    }

    override fun delete(isbn: String) = repository.deleteById(isbn)

    override fun isExist(isbn: String): Boolean = repository.existsById(isbn)
}