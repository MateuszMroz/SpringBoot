package com.mmroz.postgres.domain.dto

data class BookDto(
        val isbn: String?,
        val title: String?,
        val author: AuthorDto? = null,
)