package ru.vood.arrow.example.ru.vood.arrow.prisms

import arrow.core.Either
import arrow.optics.Prism
import arrow.optics.optics

@optics
sealed interface User {
    companion object
}
@optics data class Person(val name: String, val age: Int): User {
    companion object
}
@optics data class Company(val name: String, val country: String): User {
    companion object
}

