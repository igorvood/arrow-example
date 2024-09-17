package ru.vood.arrow.example.ru.vood.arrow.errorHandling

import arrow.core.raise.Raise

object User

data class UserExists(val userName: String)

fun Raise<UserExists>.insertUser(userName: String): User =
    raise(UserExists(userName))

context(Raise<UserExists>)
fun insertUserContext(userName: String): User =
    raise(UserExists(userName))