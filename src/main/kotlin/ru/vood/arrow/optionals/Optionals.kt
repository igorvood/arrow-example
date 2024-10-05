package ru.vood.arrow.example.ru.vood.arrow.optionals

import arrow.optics.dsl.index
import arrow.optics.optics
import arrow.optics.typeclasses.Index
import io.kotest.matchers.shouldBe

@optics
data class Db(val cities: Map<String, City>) {
    companion object
}
@optics data class City(val name: String, val country: String) {
    companion object
}


fun main() {
    val db = Db(mapOf(
        "Alejandro" to City("Hilversum", "Netherlands"),
        "Ambrosio"  to City("Ciudad Real", "Spain")
    ))

    fun example1() {
        Db.cities.index(Index.map(), "Alejandro").country.getOrNull(db) shouldBe "Netherlands"
        Db.cities.index(Index.map(), "Jack").country.getOrNull(db) shouldBe null
    }

    fun example2() {
        val dbWithJack = Db.cities.index(Index.map(), "Jack").set(db, City("London", "UK"))
        // Jack was not really added to the database
        ("Jack" in dbWithJack.cities) shouldBe false
    }

    fun example3() {
        val dbWithJack = Db.cities.modify(db) { it + ("Jack" to City("London", "UK")) }
        // now Jack is finally in the database
        ("Jack" in dbWithJack.cities) shouldBe true
    }


    example1()
    example2()
    example3()
}