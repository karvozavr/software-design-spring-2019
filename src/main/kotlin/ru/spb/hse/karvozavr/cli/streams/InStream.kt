package ru.spb.hse.karvozavr.cli.streams

/**
 * Input stream.
 */
interface InStream {

    /**
     * Reads next line in stream.
     *
     * @return next line
     */
    fun scanLine(): String

    /**
     * Return if the stream is empty.
     *
     * @return if the stream is empty
     */
    fun isEmpty(): Boolean

    /**
     * Return if the stream is not empty.
     *
     * @return if the stream is not empty
     */
    fun isNotEmpty() = !isEmpty()
}

/**
 * Input stream with no data in it.
 */
object EmptyStream : InStream {
    override fun isEmpty(): Boolean = true

    override fun scanLine(): String = ""
}