package com.mmroz.postgres.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.mmroz.postgres.domain.dto.BookDto
import com.mmroz.postgres.domain.entities.AuthorEntity
import com.mmroz.postgres.domain.entities.BookEntity
import com.mmroz.postgres.services.BookService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@ExtendWith(SpringExtension::class)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class BookControllerIntegrationTests @Autowired constructor(
        private val mockMvc: MockMvc,
        private val bookService: BookService
) {

    private val objectMapper: ObjectMapper = ObjectMapper()

    @Test
    fun `return http status 201 created when create book`() {
        val bookDto = BookDto(
                isbn = "978-1-2345-7689-0",
                title = "The Shadow in The Attic",
                author = null
        )
        val bookJson = objectMapper.writeValueAsString(bookDto)

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/${bookDto.isbn}")
                        .contentType(APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated
        )
    }

    @Test
    fun `return http status 200(ok) when update book`() {
        val bookEntity = BookEntity(
                isbn = "978-1-2345-7689-0",
                title = "The Shadow in The Attic and more...",
                authorEntity = null
        )
        val savedBookEntity = bookService.createUpdateBook("978-1-2345-7689-0", bookEntity) ?: return

        val bookDto = BookDto(
                isbn = savedBookEntity.isbn,
                title = "The Shadow in The Attic and more...",
                author = null
        )
        val bookJson = objectMapper.writeValueAsString(bookDto)

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/${bookDto.isbn}")
                        .contentType(APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk
        )
    }

    @Test
    fun `return saved book when create book`() {
        val bookDto = BookDto(
                isbn = "978-1-2345-7689-0",
                title = "The Shadow in The Attic",
                author = null
        )
        val bookJson = objectMapper.writeValueAsString(bookDto)

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/${bookDto.isbn}")
                        .contentType(APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(bookDto.isbn)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(bookDto.title)
        )
    }

    @Test
    fun `return updated book when update book`() {
        val bookEntity = BookEntity(
                isbn = "978-1-2345-7689-0",
                title = "The Shadow in The Attic and more...",
                authorEntity = null
        )
        val savedBookEntity = bookService.createUpdateBook("978-1-2345-7689-0", bookEntity) ?: return

        val bookDto = BookDto(
                isbn = savedBookEntity.isbn,
                title = "The Shadow in The Attic and more...",
                author = null
        )
        val bookJson = objectMapper.writeValueAsString(bookDto)

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/${bookDto.isbn}")
                        .contentType(APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(bookDto.isbn)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(bookDto.title)
        )
    }

    @Test
    fun `return 200(OK) when return list of books`() {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
                        .contentType(APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `return list of books when return list of authors`() {
        val bookEntity = BookEntity(
                isbn = "978-1-2345-7689-0",
                title = "The Shadow in The Attic",
                authorEntity = null
        )
        bookService.createUpdateBook("978-1-2345-7689-0", bookEntity)


        mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
                        .contentType(APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].isbn").value("978-1-2345-7689-0")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].title").value("The Shadow in The Attic")
        )
    }

    @Test
    fun `return 200(OK) when author exists`() {
        val bookEntity = BookEntity(
                isbn = "978-1-2345-7689-0",
                title = "The Shadow in The Attic",
                authorEntity = null
        )
        bookService.createUpdateBook("978-1-2345-7689-0", bookEntity)

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/978-1-2345-7689-0")
                        .contentType(APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `return 404(NOT FOUND) when book does not exist`() {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/978-1-2345-7689-2")
                        .contentType(APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `return book when author exists`() {
        val bookEntity = BookEntity(
                isbn = "978-1-2345-7689-0",
                title = "The Shadow in The Attic",
                authorEntity = null
        )
        bookService.createUpdateBook("978-1-2345-7689-0", bookEntity)

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/978-1-2345-7689-0")
                        .contentType(APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value("978-1-2345-7689-0")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value("The Shadow in The Attic")
        )
    }

    @Test
    fun `return 200(OK) on partial update when author exists`() {
        val bookEntity = BookEntity(
                isbn = "978-1-2345-7689-0",
                title = "The Shadow in The Attic",
                authorEntity = null
        )
        bookService.createUpdateBook("978-1-2345-7689-0", bookEntity)

        val bookDto = BookDto(
                isbn = "978-1-2345-7689-0",
                title = "Updated",
        )
        val bookJson = objectMapper.writeValueAsString(bookDto)

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/978-1-2345-7689-0")
                        .contentType(APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `return updated book on partial update when author exists`() {
        val bookEntity = BookEntity(
                isbn = "978-1-2345-7689-0",
                title = "The Shadow in The Attic",
                authorEntity = null
        )
        bookService.createUpdateBook("978-1-2345-7689-0", bookEntity)

        val bookDto = BookDto(
                isbn = "978-1-2345-7689-0",
                title = "Updated",
        )
        val bookJson = objectMapper.writeValueAsString(bookDto)

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/978-1-2345-7689-0")
                        .contentType(APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value("978-1-2345-7689-0")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value("Updated")
        )
    }

    @Test
    fun `return no content(204) when delete book for non existing book`() {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/books/111-1-1111-111-1")
                        .contentType(APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent)
    }

    @Test
    fun `return no content(204) when delete author for existing author`() {
        val bookEntity = BookEntity(
                isbn = "978-1-2345-7689-0",
                title = "The Shadow in The Attic",
                authorEntity = null
        )
        val savedBookEntity = bookService.createUpdateBook("978-1-2345-7689-0", bookEntity)

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/books/${savedBookEntity?.isbn}")
                        .contentType(APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent)
    }
}