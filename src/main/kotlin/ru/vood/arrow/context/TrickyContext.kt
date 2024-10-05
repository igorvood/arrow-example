package ru.vood.arrow.example.ru.vood.arrow.context

import java.util.*

context(String, Int)
fun s(): Unit {
    println("------ String, Int context")
    println(toLong())
    println(substring(1))
}

context(String)
fun s(): Unit {
    println("------ String context")
    println(uppercase(Locale.getDefault()))
}

context(Int)
fun s(): Unit {
    println("------ Int context")
    println(toLong() + 1)
}

fun main() {
    "1qwerty".apply {
        s()
        with(15) {
            s()
        }
    }

    23.apply {
        s()
    }

}