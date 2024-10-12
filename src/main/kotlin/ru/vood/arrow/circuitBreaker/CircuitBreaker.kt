package ru.vood.arrow.example.ru.vood.arrow.circuitBreaker

import arrow.core.Either
import arrow.resilience.CircuitBreaker
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

@ExperimentalTime
suspend fun main(): Unit {
    val circuitBreaker = CircuitBreaker(
        openingStrategy = CircuitBreaker.OpeningStrategy.Count(2),
        resetTimeout = 2.seconds,
        exponentialBackoffFactor = 1.2,
        maxResetTimeout = 60.seconds,
    )

    // normal operation
    circuitBreaker.protectOrThrow { "I am in Closed: ${circuitBreaker.state()::class.java.simpleName}" }.also(::println)

    // simulate service getting overloaded
    Either.catch {
        circuitBreaker.protectOrThrow { throw RuntimeException("Service overloaded") }
    }.also(::println)
    Either.catch {
        circuitBreaker.protectOrThrow { throw RuntimeException("Service overloaded") }
    }.also(::println)
    circuitBreaker.protectEither { }
        .also { println("I am Open and short-circuit with ${it}. ${circuitBreaker.state()}") }

    // simulate reset timeout
    println("Service recovering . . .").also { delay(2000) }

    // simulate test request success
    circuitBreaker.protectOrThrow {
        "I am running test-request in HalfOpen: ${circuitBreaker.state()}"
    }.also(::println)
    println("I am back to normal state closed ${circuitBreaker.state()}")
}