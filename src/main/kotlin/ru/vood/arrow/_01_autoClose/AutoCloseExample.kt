package ru.vood.arrow.example.ru.vood.arrow.autoClose

import arrow.autoCloseScope

class Db : AutoCloseable {
    override fun close() {
        println("close ${this.javaClass.name}")
    }
}

class Tracing : AutoCloseable {
    override fun close() {
        println("close ${this.javaClass.name}")
    }
}


fun classicExample() {
    Db().use {
        Tracing().use {
            println("classicExample А вот тут рабочий код")
        }
    }
}

@OptIn(ExperimentalStdlibApi::class)
fun bestExample() {
    autoCloseScope {
        val dataSource = install(Db())
        val tracing = install(Tracing())
        println("bestExample А вот тут рабочий код")
    }
}

fun main() {
    classicExample()
    bestExample()
}