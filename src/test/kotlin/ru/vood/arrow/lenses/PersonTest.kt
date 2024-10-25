package ru.vood.arrow.lenses

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import ru.vood.arrow.example.ru.vood.arrow._02_lenses.*

class PersonTest {

    @Test
    fun Operations() {
        val me = Person(
            "Alejandro", 35,
            Address(Street("Kotlinstraat", 1), City("Hilversum", "Netherlands"))
        )

        Person.name.get(me) shouldBe "Alejandro"

        val meAfterBirthdayParty = Person.age.modify(me) { it + 1 }
        Person.age.get(meAfterBirthdayParty) shouldBe 36

        val newAddress = Address(Street("Kotlinplein", null), City("Amsterdam", "Netherlands"))
        val meAfterMoving = Person.address.set(me, newAddress)
        Person.address.get(meAfterMoving) shouldBe newAddress
    }


    val personCity =
        Person.address compose Address.city compose City.name

    @Test
    fun Composition() {
        val me = Person(
            "Alejandro", 35,
            Address(Street("Kotlinstraat", 1), City("Hilversum", "Netherlands"))
        )

        personCity.get(me) shouldBe "Hilversum"
        val meAtTheCapital = personCity.set(me, "Amsterdam")
    }

    @Test
    fun compareMethods() {
        val etalon = Person(
            "Alejandro", 35,
            Address(Street("Kotlinstraat", 1), City("Hilversum", "Netherlands"))
        )


        Person.address.city.name.set(etalon,"Some city") shouldBe etalon.copy(address = etalon.address.copy(city = etalon.address.city.copy(name = "Some city")))

    }
}