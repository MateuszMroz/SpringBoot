package com.mmroz.postgres.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.mmroz.postgres.domain.dto.AuthorDto
import com.mmroz.postgres.domain.entities.AuthorEntity
import com.mmroz.postgres.services.AuthorService
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
class AuthorControllerIntegrationTests @Autowired constructor(
        private val mockMvc: MockMvc,
        private val authorService: AuthorService
) {

    private val objectMapper: ObjectMapper = ObjectMapper()

    @Test
    fun `return 201(Created) when create author successfully`() {
        val author = AuthorDto(
                name = "Abigail Rose",
                age = 80
        )
        val authorJson = objectMapper.writeValueAsString(author)

        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated
        )
    }

    @Test
    fun `return saved author when create author successfully`() {
        val author = AuthorDto(
                name = "Abigail Rose",
                age = 80
        )
        val authorJson = objectMapper.writeValueAsString(author)

        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Abigail Rose")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(80)
        )
    }

    @Test
    fun `return 200(OK) when return list of authors`() {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
                        .contentType(APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `return list of authors when return list of authors`() {
        val authorEntity = AuthorEntity(
                id = 1L,
                name = "Abigail Rose",
                age = 80
        )
        authorService.save(authorEntity)

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
                        .contentType(APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value("Abigail Rose")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].age").value(80)
        )
    }

    @Test
    fun `return 200(OK) when author exist`() {
        val authorEntity = AuthorEntity(
                id = 1L,
                name = "Abigail Rose",
                age = 80
        )
        authorService.save(authorEntity)

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/1")
                        .contentType(APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `return 404(NOT FOUND) when author no exist`() {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/1")
                        .contentType(APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `return author when author exist`() {
        val authorEntity = AuthorEntity(
                id = 1L,
                name = "Abigail Rose",
                age = 80
        )
        authorService.save(authorEntity)

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/1")
                        .contentType(APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(1L)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Abigail Rose")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(80)
        )
    }

    @Test
    fun `return 200(OK) on update when author exist`() {
        val authorEntity = AuthorEntity(
                id = 1L,
                name = "Abigail Rose",
                age = 80
        )
        authorService.save(authorEntity)

        val authorDto = AuthorDto(
                name = "Abigail Rose",
                age = 80
        )
        val authorJson = objectMapper.writeValueAsString(authorDto)

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/${authorEntity.id}")
                        .contentType(APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `return 404(NOT FOUND) on update when author no exist`() {
        val authorDto = AuthorDto(
                name = "Abigail Rose",
                age = 80
        )
        val authorJson = objectMapper.writeValueAsString(authorDto)
        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/1")
                        .contentType(APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `update author when find in database`() {
        val authorEntity = AuthorEntity(
                id = 1L,
                name = "Abigail Rose",
                age = 80
        )
        val savedAuthorEntity = authorService.save(authorEntity)

        val authorDto = AuthorDto(
                id = savedAuthorEntity.id,
                name = "Abigail Marie Rose",
                age = 82
        )
        val authorJson = objectMapper.writeValueAsString(authorDto)

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/${authorEntity.id}")
                        .contentType(APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedAuthorEntity.id)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(authorDto.name)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(authorDto.age)
        )
    }

    @Test
    fun `return 200(OK) on partial update when author exist`() {
        val authorEntity = AuthorEntity(
                id = 1L,
                name = "Abigail Rose",
                age = 80
        )
        val savedAuthorEntity = authorService.save(authorEntity)

        val authorDto = AuthorDto(
                name = "Abigail Rose",
                age = 80
        )
        val authorJson = objectMapper.writeValueAsString(authorDto)

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/${savedAuthorEntity.id}")
                        .contentType(APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `return updated author on partial update when author exist`() {
        val authorEntity = AuthorEntity(
                id = 1L,
                name = "Abigail Rose",
                age = 80
        )
        val savedAuthorEntity = authorService.save(authorEntity)

        val authorDto = AuthorDto(
                name = "UPDATE",
                age = 80
        )
        val authorJson = objectMapper.writeValueAsString(authorDto)

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/${savedAuthorEntity.id}")
                        .contentType(APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect {
            MockMvcResultMatchers.jsonPath("$.id").value(savedAuthorEntity.id)
            MockMvcResultMatchers.jsonPath("$.name").value("UPDATE")
            MockMvcResultMatchers.jsonPath("$.age").value(authorDto.age)
        }
    }

    @Test
    fun `return no content(204) when delete author for non existing author`() {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/authors/999")
                        .contentType(APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent)
    }

    @Test
    fun `return no content(204) when delete author for existing author`() {
        val authorEntity = AuthorEntity(
                id = 1L,
                name = "Abigail Rose",
                age = 80
        )
        val savedAuthorEntity = authorService.save(authorEntity)

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/authors/${savedAuthorEntity.id}")
                        .contentType(APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent)
    }
}