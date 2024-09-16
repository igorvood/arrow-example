package ru.vood.arrow.prisms

import arrow.core.Either
import arrow.optics.Prism
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import ru.vood.arrow.example.ru.vood.arrow.prisms.User
import ru.vood.arrow.example.ru.vood.arrow.prisms.age
import ru.vood.arrow.example.ru.vood.arrow.prisms.person

class PersonTest {

    fun List<User>.happyBirthday() =
        map { User.person.age.modify(it) { age -> age + 1 } }

    @Test
    fun example() {
        val x = Prism.left<Int, String>().reverseGet(5)
        x shouldBe Either.Left(5)
    }


}