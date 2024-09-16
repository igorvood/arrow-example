package ru.vood.arrow.reflection

import arrow.optics.lens
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import ru.vood.arrow.example.ru.vood.arrow.reflection.Person

class PersonTest {

    @Test
    fun example() {
        val p = Person("me", listOf("pat", "mat"))
        val m = Person::name.lens.modify(p) { it.capitalize() }
        m.name shouldBe "Me"
    }
}