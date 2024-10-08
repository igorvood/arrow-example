package ru.vood.arrow.example.ru.vood.arrow.saga

import arrow.core.raise.Raise
import arrow.core.raise.recover
import arrow.resilience.SagaScope
import arrow.resilience.saga
import arrow.resilience.transact
import kotlinx.coroutines.runBlocking
import ru.vood.arrow.example.ru.vood.arrow.errorHandling.*


context(Raise<UserNameMissing>)
suspend fun SagaScope.insertOrRollBack(userName: String): User {
    val saga = saga({ insertUser(userName) }) { user ->
        println("""удаляю пользователя "$userName" """)
    }
    return saga
}


context(Raise<UserRegistrationError>)
suspend fun route2(request: HttpRequest): HttpResponse {
    val name = request.userName()
    saga {

        val user = insertOrRollBack(name)
        val user2 = insertOrRollBack("Получатель платежа от Тетушки")
        user.receivePayment()
//        raise(ExpiredCard)
    }.transact()
    return HttpResponse.CREATED

}

fun main() {
    runBlocking {
        recover({ route2(HttpRequest) }) {
            println("цепочка создание пользователя, регисстрация его и проведение платежа по причине ${it.javaClass.name}")
        }
    }
}