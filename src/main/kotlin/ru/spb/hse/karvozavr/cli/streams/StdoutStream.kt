package ru.spb.hse.karvozavr.cli.streams

/**
 * Stream writing to stdout.
 */
class StdoutStream : OutStream {
    override fun write(line: String) {
        println(line)
    }
}