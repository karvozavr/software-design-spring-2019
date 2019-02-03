package ru.spb.hse.karvozavr.cli.streams

import java.util.*

/**
 * Writable and readable stream buffer.
 */
class ReadWriteStream : InStream, OutStream {
    private val buffer: Deque<String> = ArrayDeque<String>()

    override fun isEmpty(): Boolean =
        buffer.isEmpty()

    override fun scanLine(): String =
        if (buffer.isNotEmpty()) {
            buffer.removeFirst()
        } else {
            throw NoSuchElementException("ReadLine on an empty stream is not allowed.")
        }

    override fun write(line: String) {
        buffer.addLast(line)
    }
}
