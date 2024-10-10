package ru.vood.arrow.example.ru.vood.arrow.errorHandling

import arrow.core.raise.Raise
import arrow.core.raise.ensure
import arrow.core.raise.recover
import kotlinx.coroutines.runBlocking

data class User(val userName: String)

sealed interface UserRegistrationError

sealed interface UserError : UserRegistrationError
data class UserExists(val userName: String) : UserError
data object UserNameMissing : UserError


sealed interface PaymentError : UserRegistrationError
data object ExpiredCard : PaymentError
data object InsufficientFunds : PaymentError

fun Raise<UserExists>.insertUser(userName: String): User {
    println("""Создаю пользователя "$userName" """)
    ensure(userName.isNotEmpty()) { UserExists(userName) }
    return User(userName)
}//User

context(Raise<UserExists>)
fun insertUserContext(userName: String): User =
    raise(UserExists(userName))

context(Raise<UserNameMissing>)
fun HttpRequest.userName(): String {
//    raise(UserNameMissing)
    return "Тетушка медоуз с дочками"
}


context(Raise<PaymentError>)
fun User.receivePayment(): Int {
    println("Пыжусь провести платеж")

//    raise(ExpiredCard)
//    raise(InsufficientFunds)

    println("А платеж то прошел")

    return 18
}

object HttpRequest

enum class HttpResponse {
    CREATED
}

context(Raise<UserExists>, Raise<PaymentError>)
suspend fun Raise<UserRegistrationError>.route(request: HttpRequest): HttpResponse {
    val userName = request.userName()
    val user = insertUser(userName)
    user.receivePayment()
    return HttpResponse.CREATED
}


fun main() {
    println("=======insertUser=====================")
    val recover = recover({ insertUser("Классический Рейз") }) { qw ->
        println("""а пользователь то "${qw.userName}" не существует""")
        null
    }

    recover({ insertUserContext("Рейз изконтекста") }) { qw ->
        println("""а пользователь то "${qw.userName}" не существует""")
        null
    }
    println("============receivePayment================")
    val user = User("asd")
    val receivePayment = recover({ user.receivePayment() }) {
        println("платеж не прошел по причине ${it.javaClass.name}")
        null
    }
    println(receivePayment)
    println("===========route=================")

    runBlocking {
        recover({ route(HttpRequest) }) {
            println("цепочка создание пользователя, регисстрация его и проведение платежа по причине ${it.javaClass.name}")
        }
    }

}