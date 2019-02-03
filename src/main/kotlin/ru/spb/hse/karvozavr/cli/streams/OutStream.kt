package ru.spb.hse.karvozavr.cli.streams

/**
 * Output stream.
 */
interface OutStream {

    /**
     * Writes out given string.
     *
     * @line string to write
     */
    fun write(line: String)
}