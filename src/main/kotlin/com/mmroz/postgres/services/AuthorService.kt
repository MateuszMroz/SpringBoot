package com.mmroz.postgres.services

import com.mmroz.postgres.domain.entities.AuthorEntity

interface AuthorService {
    fun save(authorEntity: AuthorEntity): AuthorEntity
    fun findAll(): List<AuthorEntity>
    fun findById(authorId: Long): AuthorEntity?
    fun partialUpdate(authorId: Long, authorEntity: AuthorEntity): AuthorEntity
    fun delete(authorId: Long)
    fun isExist(authorId: Long): Boolean
}