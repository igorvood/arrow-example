package ru.vood.arrow.reflection

import arrow.core.merge
import arrow.fx.coroutines.parZip
import arrow.fx.coroutines.raceN
import arrow.optics.Every
import arrow.optics.instance
import arrow.optics.lens
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

import ru.vood.arrow.example.ru.vood.arrow.reflection.Person

class PersonTest {

    @Test
    fun example() {
        val p = Person("me", listOf("pat", "mat"))
        val m = Person::name.lens.modify(p) { it.capitalize() }
        m.name shouldBe "Me"
    }


    sealed interface Cutlery
    object Fork: Cutlery
    object Spoon: Cutlery

    @Test
    fun Prisms() {
        val things = listOf(Fork, Spoon, Fork)
        val forks = Every.list<Cutlery>() compose instance<Cutlery, Fork>()
        val noOfForks = forks.size(things)
        noOfForks shouldBe 2
    }

    data class UserQ(val name: String, val avatar: String, val child: String)

    suspend fun getUser(id: String): UserQ =
        parZip(
            { "getUserName(id)" },
            { "getAvatar(id)" },
            { "getUserChild(id)" },

        ) { name, avatar, child -> UserQ(name, avatar, child) }

    suspend fun file(server1: String, server2: String) =
        raceN(
            { "downloadFrom(server1)" },
            { "downloadFrom(server2)" }
        ).merge()
}