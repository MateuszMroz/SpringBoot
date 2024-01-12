package com.mmroz.postgres.domain.entities

import jakarta.persistence.*
import jakarta.persistence.GenerationType.SEQUENCE

@Entity
@Table(name = "authors")
data class AuthorEntity(
        @Id
        @GeneratedValue(strategy = SEQUENCE, generator = "author_id_seq")
        val id: Long? = null,
        val name: String?,
        val age: Int?
)