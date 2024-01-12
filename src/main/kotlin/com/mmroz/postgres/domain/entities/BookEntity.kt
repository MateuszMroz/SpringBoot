package com.mmroz.postgres.domain.entities

import jakarta.persistence.CascadeType.ALL
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "books")
data class BookEntity(
        @Id
        val isbn: String?,
        val title: String?,
        @ManyToOne(cascade = [ALL])
        @JoinColumn(name = "author_id")
        val authorEntity: AuthorEntity?,
)
