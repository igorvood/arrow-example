package ru.vood.arrow._07_validation

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import ru.vood.arrow.example.ru.vood.arrow.validation.Book

class BookTest {

    @Test
    fun getTitle() {

        val book = Book.invoke("", listOf())
        assertEquals(true, book.isLeft())
        assertEquals(2, book.leftOrNull()?.size)


    }
}