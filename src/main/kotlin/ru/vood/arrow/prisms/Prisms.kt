package ru.vood.arrow.example.ru.vood.arrow.prisms

import arrow.core.Either
import arrow.optics.PTraversal
import arrow.optics.Prism
import arrow.optics.optics
import io.kotest.matchers.shouldBe

@optics
sealed interface SomeAbstraction {
    val name: String
    companion object
}

@optics
data class Person(override val name: String, val age: Int) : SomeAbstraction {
    companion object
}

@optics
data class Company(override val name: String, val country: String) : SomeAbstraction {
    companion object
}

@optics
data class Company2(override val name: String, val country: String) : SomeAbstraction {
    companion object
}

@optics
data class Company3(override val name: String, val country: String) : SomeAbstraction {
    companion object
}

fun List<SomeAbstraction>.happyBirthday() =
map { SomeAbstraction.person.age.modify(it) { age -> age + 1 } }

fun example() {
    val x = Prism.left<Int, String>().reverseGet(5)
    x shouldBe Either.Left(5)
}
fun main() {
    val company = Company("CompanyName", "CompanyCountry")
    val person = Person("PersonName", 45)

    println("===============modify only person==================")
    println(SomeAbstraction.person.name.modify(company, String::uppercase))
    println(SomeAbstraction.person.name.modify(person, String::uppercase))
    println("===============modify any type==================")
    println(SomeAbstraction.name.modify(company, String::uppercase))
    println(SomeAbstraction.name.modify(person, String::uppercase))
    println("===============getAll==================")
    println(SomeAbstraction.person.age.getAll(company))
    println(SomeAbstraction.person.age.getAll(person))
    println("===============getOrNull==================")
    println(SomeAbstraction.person.age.getOrNull(company))
    println(SomeAbstraction.person.age.getOrNull(person))

    println("===============choice==================")
//    val message = SomeAbstraction.person.age.choice(P)
//    println(message)



}
