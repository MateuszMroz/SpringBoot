package com.mmroz.postgres.services.impl

import com.mmroz.postgres.domain.entities.AuthorEntity
import com.mmroz.postgres.repositories.AuthorRepository
import com.mmroz.postgres.services.AuthorService
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class AuthServiceImpl(private val repository: AuthorRepository) : AuthorService {

    override fun save(authorEntity: AuthorEntity): AuthorEntity = repository.save(authorEntity)

    override fun findAll(): List<AuthorEntity> = repository.findAll().toList()

    override fun findById(authorId: Long): AuthorEntity? = repository.findById(authorId).getOrNull()

    override fun partialUpdate(authorId: Long, authorEntity: AuthorEntity): AuthorEntity {
        val original = repository.findById(authorId).getOrNull()
                ?: throw NoSuchElementException("Could not find author with accountId")

        return repository.save(original.copy(
                id = authorEntity.id ?: original.id,
                name = authorEntity.name ?: original.name,
                age = authorEntity.age ?: original.age
        ))
    }

    override fun delete(authorId: Long) {
        repository.deleteById(authorId)
    }

    override fun isExist(authorId: Long): Boolean = repository.existsById(authorId)
}