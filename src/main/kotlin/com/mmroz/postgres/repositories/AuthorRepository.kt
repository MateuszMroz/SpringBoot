package com.mmroz.postgres.repositories

import com.mmroz.postgres.domain.entities.AuthorEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AuthorRepository : CrudRepository<AuthorEntity, Long> {
    fun ageLessThan(age: Int): List<AuthorEntity>

    @Query("SELECT a FROM AuthorEntity a WHERE a.age > ?1")
    fun findAuthorsWithAgeGraterThan(age: Int): List<AuthorEntity>
}