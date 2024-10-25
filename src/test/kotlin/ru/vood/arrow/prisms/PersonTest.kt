package ru.vood.arrow.prisms

import arrow.core.Either
import arrow.optics.Prism
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import ru.vood.arrow.example.ru.vood.arrow._03_prisms.SomeAbstraction
import ru.vood.arrow.example.ru.vood.arrow._03_prisms.age
import ru.vood.arrow.example.ru.vood.arrow._03_prisms.person

class PersonTest {

    fun List<SomeAbstraction>.happyBirthday() =
        map { SomeAbstraction.person.age.modify(it) { age -> age + 1 } }

    @Test
    fun example() {
        val x = Prism.left<Int, String>().reverseGet(5)
        x shouldBe Either.Left(5)
    }


}