package com.mmroz.postgres.mappers.impl

import com.mmroz.postgres.domain.dto.AuthorDto
import com.mmroz.postgres.domain.entities.AuthorEntity
import com.mmroz.postgres.mappers.Mapper
import org.springframework.stereotype.Component

@Component
class AuthorMapper : Mapper<AuthorEntity, AuthorDto> {

    override fun mapTo(a: AuthorEntity): AuthorDto {
        return AuthorDto(
                id = a.id,
                name = a.name,
                age = a.age
        )
    }

    override fun mapFrom(b: AuthorDto): AuthorEntity {
        return AuthorEntity(
                id = b.id,
                name = b.name,
                age = b.age
        )
    }

}