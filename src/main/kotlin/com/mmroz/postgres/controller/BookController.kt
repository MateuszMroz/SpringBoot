package com.mmroz.postgres.controller

import com.mmroz.postgres.domain.dto.BookDto
import com.mmroz.postgres.mappers.impl.BookMapper
import com.mmroz.postgres.services.BookService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class BookController(
        private val bookService: BookService,
        private val bookMapper: BookMapper
) {
    @PutMapping(path = ["/books/{isbn}"])
    fun fullUpdateBook(
            @PathVariable("isbn") isbn: String,
            @RequestBody bookDto: BookDto
    ): ResponseEntity<BookDto> {
        val isBookExist = bookService.isExist(isbn)
        val savedBookEntity = bookService.createUpdateBook(isbn, bookMapper.mapFrom(bookDto.copy(isbn = isbn)))
                ?: return ResponseEntity(NOT_FOUND)
        val savedUpdatedBookDto = bookMapper.mapTo(savedBookEntity)

        return if (isBookExist) {
            ResponseEntity(savedUpdatedBookDto, OK)
        } else {
            ResponseEntity(savedUpdatedBookDto, CREATED)
        }
    }

    @PatchMapping(path = ["/books/{isbn}"])
    fun partialUpdateBook(
            @PathVariable("isbn") isbn: String,
            @RequestBody bookDto: BookDto
    ): ResponseEntity<BookDto> {
        if (!bookService.isExist(isbn)) {
            return ResponseEntity(NOT_FOUND)
        }

        val savedBook = bookService.partialUpdate(isbn, bookMapper.mapFrom(bookDto)) ?: return ResponseEntity(NOT_FOUND)
        return ResponseEntity(bookMapper.mapTo(savedBook), OK)

    }

    @GetMapping(path = ["/books"])
    fun listBook(pageable: Pageable): Page<BookDto> = bookService.findAll(pageable).map { bookMapper.mapTo(it) }


    @GetMapping(path = ["/books/{isbn}"])
    fun getBook(@PathVariable("isbn") isbn: String): ResponseEntity<BookDto> {
        val result = bookService.findBookById(isbn)
        return result?.let { ResponseEntity(bookMapper.mapTo(result), OK) } ?: ResponseEntity(NOT_FOUND)
    }

    @DeleteMapping(path = ["/books/{isbn}"])
    fun deleteBook(@PathVariable("isbn") isbn: String): ResponseEntity<BookDto> {
        bookService.delete(isbn)
        return ResponseEntity(NO_CONTENT)
    }
}