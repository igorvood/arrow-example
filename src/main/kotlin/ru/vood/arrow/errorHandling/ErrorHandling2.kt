package ru.vood.arrow.example.ru.vood.arrow.errorHandling

import arrow.core.raise.Raise
import arrow.core.raise.ensure
import arrow.core.raise.ensureNotNull
import arrow.core.raise.recover
import arrow.core.right

context(Raise<String>)
fun everthing(){
    val x = 1.right().bind()
    val y = ensureNotNull(2) { "Value was null" }
    ensure(y>=0){"y should be >=0"}
}


fun main(){
    recover ({everthing()}){
        println("""Опаньки ошибочка вышла "$it""")
    }
}