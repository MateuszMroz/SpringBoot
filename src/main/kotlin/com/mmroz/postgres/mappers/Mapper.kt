package com.mmroz.postgres.mappers

interface Mapper<A, B> {
    fun mapTo(a: A): B
    fun mapFrom(b: B): A
}