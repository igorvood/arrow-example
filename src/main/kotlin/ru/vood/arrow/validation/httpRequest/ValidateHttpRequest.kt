package ru.vood.arrow.example.ru.vood.arrow.validation.httpRequest

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.raise.zipOrAccumulate

sealed interface HttpRequestError
sealed interface HeaderError : HttpRequestError {
    val headerName: String
}


data object BodyError : HttpRequestError
data class HeaderNecessary(override val headerName: String) : HeaderError
data class HeaderIsNotEmpty(override val headerName: String) : HeaderError

@JvmInline
value class Header1Val(val value: String)

@JvmInline
value class Header2Val(val value: String)

@JvmInline
value class ParsedBodyToDto(val value: String)


data class MyHttpRequest(
    val headers: Map<String, String?>,
    val body: String?
)


fun header1Validate(headers: Map<String, String?>): Either<HeaderError, Header1Val> = either {
    val headerName = "Header1"
    val header1Str = headers[headerName]
    ensure(header1Str != null) { HeaderNecessary(headerName) }
    ensure(header1Str.isNotEmpty()) { HeaderIsNotEmpty(headerName) }
    Header1Val(header1Str)
}

fun header2Validate(headers: Map<String, String?>): Either<HeaderError, Header2Val> = either {
    val headerName = "Header2"
    val header1Str = headers[headerName]
    ensure(header1Str != null) { HeaderNecessary(headerName) }
    ensure(header1Str.isNotEmpty()) { HeaderIsNotEmpty(headerName) }
    Header2Val(header1Str)
}

fun bodyValidate(body: String?): Either<BodyError, ParsedBodyToDto> = either {
    ensure(body != null) { BodyError }
    ensure(body.isNotEmpty()) { BodyError }
    ParsedBodyToDto(body)
}

data class ParsedHttpRequest(val h1: Header1Val, val h2: Header2Val)

fun httpRequestValidate(
    httpRequest: MyHttpRequest
): Either<NonEmptyList<HttpRequestError>, ParsedHttpRequest> = either {
    zipOrAccumulate(
        { header1Validate(httpRequest.headers).bind() },
        { header2Validate(httpRequest.headers).bind() },
        { bodyValidate(httpRequest.body).bind() },
    ) { h1, h2, dto ->
        ParsedHttpRequest(h1, h2)
    }
}

fun main() {

    val httpRequest = MyHttpRequest(
        headers = mapOf(
            "Header1" to "1",
            "Header2" to "2",
//            "Header2" to ""
        ),
        body = "null"
    )
    val httpRequestValidate = httpRequestValidate(httpRequest)

    println(httpRequestValidate)
}