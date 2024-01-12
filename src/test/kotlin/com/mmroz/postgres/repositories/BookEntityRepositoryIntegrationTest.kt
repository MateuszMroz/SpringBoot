package com.mmroz.postgres.repositories

import com.mmroz.postgres.domain.entities.AuthorEntity
import com.mmroz.postgres.domain.entities.BookEntity
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
class BookEntityRepositoryIntegrationTest @Autowired constructor(private val underTest: BookRepository) {

    @Test
    fun `find book when it was created`() {
        val authorEntity = AuthorEntity(
                id = 1L,
                name = "Abigail Rose",
                age = 80
        )
        val bookEntity = BookEntity(
                isbn = "978-1-2345-7689-0",
                title = "The Shadow in The Attic",
                authorEntity = authorEntity
        )

        underTest.save(bookEntity)
        val result = underTest.findById("978-1-2345-7689-0")

        assertThat(result.get()).isNotNull
        assertThat(result.get().isbn).isEqualTo(bookEntity.isbn)
        assertThat(result.get().title).isEqualTo(bookEntity.title)
        assertThat(result.get().authorEntity).isEqualTo(bookEntity.authorEntity)
    }

    @Test
    fun `find many books when they was created`() {
        val authorEntity = AuthorEntity(
                id = 1L,
                name = "Abigail Rose",
                age = 80
        )
        val bookEntity1 = BookEntity(
                isbn = "978-1-2345-7689-0",
                title = "The Shadow in The Attic",
                authorEntity = authorEntity
        )
        underTest.save(bookEntity1)

        val bookEntity2 = BookEntity(
                isbn = "897-1-1111-7649-0",
                title = "Beyond the Horizon",
                authorEntity = authorEntity
        )
        underTest.save(bookEntity2)

        val result = underTest.findAll()

        assertThat(result)
                .hasSize(2)
                .containsExactly(bookEntity1, bookEntity2)
    }

    @Test
    fun `update book when it was created`() {
        val authorEntity = AuthorEntity(
                id = 1L,
                name = "Abigail Rose",
                age = 80
        )

        val bookEntity = BookEntity(
                isbn = "978-1-2345-7689-0",
                title = "The Shadow in The Attic",
                authorEntity = authorEntity
        )
        underTest.save(bookEntity)

        underTest.save(bookEntity.copy(title = "UPDATED"))
        val result = underTest.findById("978-1-2345-7689-0")

        assertThat(result.get().title).isEqualTo("UPDATED")
    }

    @Test
    fun `delete book when it was created`() {
        val authorEntity = AuthorEntity(
                id = 1L,
                name = "Abigail Rose",
                age = 80
        )

        val bookEntity = BookEntity(
                isbn = "978-1-2345-7689-0",
                title = "The Shadow in The Attic",
                authorEntity = authorEntity
        )
        underTest.save(bookEntity)
        underTest.deleteById("978-1-2345-7689-0")

        val result = underTest.findById("978-1-2345-7689-0")
        assertThat(result).isEmpty
    }
}