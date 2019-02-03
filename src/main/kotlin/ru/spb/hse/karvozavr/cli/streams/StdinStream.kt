package ru.spb.hse.karvozavr.cli.streams

import java.util.*

/**
 * Stream providing user input from stdin.
 */
class StdinStream : InStream {

    private val scanner: Scanner = Scanner(System.`in`)

    override fun scanLine(): String {
        val line = scanner.nextLine()

        if (line != null)
            return line
        else
            throw NoSuchElementException("It's not allowed to read from empty stream.")
    }

    override fun isEmpty(): Boolean = scanner.hasNext()
}