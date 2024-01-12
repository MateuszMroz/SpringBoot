package com.mmroz.postgres.mappers.impl

import com.mmroz.postgres.domain.dto.BookDto
import com.mmroz.postgres.domain.entities.BookEntity
import com.mmroz.postgres.mappers.Mapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class BookMapper @Autowired constructor(private val authorMapper: AuthorMapper) : Mapper<BookEntity, BookDto> {

    override fun mapTo(a: BookEntity): BookDto {
        return BookDto(
                isbn = a.isbn,
                title = a.title,
                author = a.authorEntity?.let { authorMapper.mapTo(it) }
        )
    }

    override fun mapFrom(b: BookDto): BookEntity {
        return BookEntity(
                isbn = b.isbn,
                title = b.title,
                authorEntity = b.author?.let { authorMapper.mapFrom(it) }
        )
    }
}