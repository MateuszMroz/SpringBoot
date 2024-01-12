package com.mmroz.postgres

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PostgresApplication

fun main(args: Array<String>) {
    runApplication<PostgresApplication>(*args)
}
