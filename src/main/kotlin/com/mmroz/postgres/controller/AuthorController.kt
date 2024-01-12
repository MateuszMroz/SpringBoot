package com.mmroz.postgres.controller

import com.mmroz.postgres.domain.dto.AuthorDto
import com.mmroz.postgres.mappers.impl.AuthorMapper
import com.mmroz.postgres.services.AuthorService
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class AuthorController(
        private val authorService: AuthorService,
        private val authorMapper: AuthorMapper
) {

    @PostMapping(path = ["/authors"])
    fun createAuthor(@RequestBody authorDto: AuthorDto): ResponseEntity<AuthorDto> {
        val result = authorService.save(authorMapper.mapFrom(authorDto))
        return ResponseEntity(authorMapper.mapTo(result), CREATED)
    }

    @GetMapping(path = ["/authors"])
    fun listAuthors(): List<AuthorDto> = authorService.findAll().map(authorMapper::mapTo)

    @GetMapping(path = ["/authors/{id}"])
    fun getAuthor(@PathVariable("id") authorId: Long): ResponseEntity<AuthorDto> {
        val result = authorService.findById(authorId)
        return result?.let { ResponseEntity(authorMapper.mapTo(result), OK) } ?: ResponseEntity(NOT_FOUND)
    }

    @PutMapping(path = ["authors/{id}"])
    fun fullUpdateAuthor(
            @PathVariable("id") authorId: Long,
            @RequestBody authorDto: AuthorDto
    ): ResponseEntity<AuthorDto> {
        return if (!authorService.isExist(authorId)) {
            ResponseEntity(NOT_FOUND)
        } else {
            val result = authorService.save(authorMapper.mapFrom(authorDto.copy(id = authorId)))
            ResponseEntity(authorMapper.mapTo(result), OK)
        }
    }

    @PatchMapping(path = ["authors/{id}"])
    fun partialUpdate(
            @PathVariable("id") authorId: Long,
            @RequestBody authorDto: AuthorDto
    ): ResponseEntity<AuthorDto> {
        return if (!authorService.isExist(authorId)) {
            ResponseEntity(NOT_FOUND)
        } else {
            val result = authorService.partialUpdate(authorId, authorMapper.mapFrom(authorDto))
            ResponseEntity(authorMapper.mapTo(result), OK)
        }
    }

    @DeleteMapping(path = ["authors/{id}"])
    fun deleteAuthor(@PathVariable("id") authorId: Long): ResponseEntity<Any> {
        authorService.delete(authorId)
        return ResponseEntity(NO_CONTENT)
    }
}