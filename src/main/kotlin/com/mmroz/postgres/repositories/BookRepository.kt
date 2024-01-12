package com.mmroz.postgres.repositories

import com.mmroz.postgres.domain.entities.BookEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRepository : CrudRepository<BookEntity, String>, PagingAndSortingRepository<BookEntity, String>