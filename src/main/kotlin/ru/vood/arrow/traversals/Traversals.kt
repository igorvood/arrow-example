package ru.vood.arrow.example.ru.vood.arrow.traversals

import arrow.optics.Every
import arrow.optics.dsl.every
import arrow.optics.optics

@optics
data class Person(val name: String, val age: Int, val friends: List<Person>) {    companion object}

fun List<Person>.happyBirthdayMap(): List<Person> =
    map { Person.age.modify(it) { age -> age + 1 } }

fun List<Person>.happyBirthdayOptics(): List<Person> =
    Every.list<Person>().age.modify(this) { age -> age + 1 }

fun Person.happyBirthdayFriends(): Person =
    copy(
        friends = friends.happyBirthdayMap()
    )

fun Person.happyBirthdayFriendsOptics(): Person =
    Person.friends.every(Every.list()).age.modify(this) { it + 1 }

fun main(){
    val person = Person(
        "Шукшин Василий Макарович", 95,
        listOf(
            Person("Макар Леонтьевич Шукшин ", 112, listOf(Person("Наталья Макаровна Шукшина", 93, listOf()))),

        )
    )
    println(person)
    println(person.happyBirthdayFriends())
    println(person.happyBirthdayFriendsOptics())
    println(person.friends.happyBirthdayOptics())


}