package com.example.androiddevchallenge.common

// I'm using this class due to a bug with the Kotlin Result class
// https://youtrack.jetbrains.com/issue/KT-45259
sealed class Either<out L, out R> {

    data class Left<out L>(val l: L) : Either<L, Nothing>()

    data class Right<out R>(val r: R) : Either<Nothing, R>()

    fun <T> fold(onLeft: (L) -> T, onRight: (R) -> T): T {
        return when (this) {
            is Left -> onLeft(l)
            is Right -> onRight(r)
        }
    }
}