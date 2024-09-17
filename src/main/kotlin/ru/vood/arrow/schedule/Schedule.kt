package ru.vood.arrow.example.ru.vood.arrow.schedule

import arrow.resilience.Schedule
import arrow.resilience.retry
import arrow.resilience.retryRaise
import kotlinx.coroutines.runBlocking
import org.jetbrains.annotations.Async
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.math.abs
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit

fun convert(): LocalDateTime {
    val now = LocalDateTime.now()
    return if ((abs(now.hashCode()) % 2) == 0) {
        now
    } else {
        val message = "Вай-вай не получилось  " + now
        println(message)
        throw RuntimeException(message)
    }
}


fun main() {

    val BASE_DELAY = 10.milliseconds
    runBlocking {
        val retry: LocalDateTime = Schedule.exponential<RuntimeException>(BASE_DELAY)
            .and(Schedule.recurs(10))
            .jittered()
//            .doWhile{ q: RuntimeException, w: Pair<Duration, Long> ->  "тут можно описать кастомное условие повторения"}
//            .retryRaise { raise(PaymentError ())  }
            .retry { convert() }
        retry
    }
}