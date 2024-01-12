package com.mmroz.postgres.repositories

import com.mmroz.postgres.domain.entities.AuthorEntity
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest
@ExtendWith(SpringExtension::class)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
class AuthorEntityRepositoryIntegrationTest @Autowired constructor(private val underTest: AuthorRepository) {

    @Test
    fun `find author when it was created`() {
        val authorEntity = AuthorEntity(
                id = 1L,
                name = "Abigail Rose",
                age = 80
        )

        underTest.save(authorEntity)
        val result = underTest.findById(1L)

        assertThat(result.get()).isNotNull
        assertThat(result.get().id).isEqualTo(authorEntity.id)
        assertThat(result.get().age).isEqualTo(authorEntity.age)
        assertThat(result.get().name).isEqualTo(authorEntity.name)
    }

    @Test
    fun `find many authors when they was created`() {
        val authorEntity1 = AuthorEntity(
                id = 1L,
                name = "Abigail Rose",
                age = 80
        )
        underTest.save(authorEntity1)

        val authorEntity2 = AuthorEntity(
                id = 2L,
                name = "Thomas Cronin",
                age = 44
        )
        underTest.save(authorEntity2)

        val result = underTest.findAll()

        assertThat(result)
                .hasSize(2)
                .containsExactly(authorEntity1, authorEntity2)
    }

    @Test
    fun `update author when it was created`() {
        val authorEntity = AuthorEntity(
                id = 1L,
                name = "Abigail Rose",
                age = 80
        )
        underTest.save(authorEntity)
        underTest.save(authorEntity.copy(name = "UPDATED"))

        val result = underTest.findById(1L)
        assertThat(result.get().name).isEqualTo("UPDATED")
    }

    @Test
    fun `delete author when it was created`() {
        val authorEntity = AuthorEntity(
                id = 1L,
                name = "Abigail Rose",
                age = 80
        )
        underTest.save(authorEntity)
        underTest.deleteById(1L)

        val result = underTest.findById(1L)

        assertThat(result).isEmpty
    }

    @Test
    fun `get authors when age is less than x`() {
        val authorEntity1 = AuthorEntity(
                id = 1L,
                name = "Abigail Rose",
                age = 80
        )
        underTest.save(authorEntity1)

        val authorEntity2 = AuthorEntity(
                id = 2L,
                name = "Thomas Cronin",
                age = 44
        )
        underTest.save(authorEntity2)

        val authorEntity3 = AuthorEntity(
                id = 3L,
                name = "Jesse A Casey",
                age = 24
        )
        underTest.save(authorEntity3)

        val result = underTest.ageLessThan(50)

        assertThat(result).containsExactly(authorEntity2, authorEntity3)
    }

    @Test
    fun `get authors with age is greater than x`() {
        val authorEntity1 = AuthorEntity(
                id = 1L,
                name = "Abigail Rose",
                age = 80
        )
        underTest.save(authorEntity1)

        val authorEntity2 = AuthorEntity(
                id = 2L,
                name = "Thomas Cronin",
                age = 44
        )
        underTest.save(authorEntity2)

        val authorEntity3 = AuthorEntity(
                id = 3L,
                name = "Jesse A Casey",
                age = 24
        )
        underTest.save(authorEntity3)

        val result = underTest.findAuthorsWithAgeGraterThan(50)

        assertThat(result).containsExactly(authorEntity1)
    }
}