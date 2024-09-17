package ru.vood.arrow.example.ru.vood.arrow.errorHandling

import arrow.core.raise.Raise
import arrow.core.raise.recover

object User

sealed interface UserError
data class UserExists(val userName: String): UserError
data object UserNameMissing : UserError


sealed interface PaymentError
data object ExpiredCard: PaymentError
data object InsufficientFunds: PaymentError

fun Raise<UserExists>.insertUser(userName: String): User =
    raise(UserExists(userName))

context(Raise<UserExists>)
fun insertUserContext(userName: String): User =
    raise(UserExists(userName))

context(Raise<PaymentError>)
fun User.receivePayment(): Int {
    println("Пыжусь провести платеж")

//    raise(ExpiredCard)
    raise(InsufficientFunds)

    println("А платеж то прошел")

    return 18
}




fun main() {
    val recover = recover({ insertUser("Классический Рейз") }) { qw ->
        println("""а пользователь то "${qw.userName}" не существует""")
        null }

    recover({ insertUserContext("Рейз изконтекста") }) { qw ->
        println("""а пользователь то "${qw.userName}" не существует""")
        null }
    println("============================")
    val user = User
    val recover1 = recover({ user.receivePayment() }) {
        println("платеж не прошел по причине ${it.javaClass.name}")
        null
    }
    println(recover1)
    println("============================")

}