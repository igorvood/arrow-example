package ru.vood.arrow.example.ru.vood.arrow.validation

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.raise.ensureNotNull
import arrow.core.raise.zipOrAccumulate
import arrow.core.recover
import arrow.core.toNonEmptyListOrNull

object EmptyAuthorName

data class Author private constructor(val name: String) {
    companion object {
        operator fun invoke(name: String): Either<EmptyAuthorName, Author> = either {
            ensure(name.isNotEmpty()) { EmptyAuthorName }
            Author(name)
        }
    }
}

sealed interface BookValidationError
object EmptyTitle : BookValidationError
object NoAuthors : BookValidationError
data class EmptyAuthor(val index: Int) : BookValidationError

data class Book private constructor(
    val title: String, val authors: NonEmptyList<Author>
) {
    companion object {
        operator fun invoke(
            title: String, authors: Iterable<String>
        ): Either<NonEmptyList<BookValidationError>, Book> = either {
            zipOrAccumulate(
                { ensure(title.isNotEmpty()) { EmptyTitle } },
                {
                    val validatedAuthors = mapOrAccumulate(authors.withIndex()) { nameAndIx ->
                        Author(nameAndIx.value)
                            .recover { _ -> raise(EmptyAuthor(nameAndIx.index)) }
                            .bind()
                    }
                    ensureNotNull(validatedAuthors.toNonEmptyListOrNull()) { NoAuthors }
                }
            ) { _, authorsNel ->
                Book(title, authorsNel)
            }
        }
    }
}