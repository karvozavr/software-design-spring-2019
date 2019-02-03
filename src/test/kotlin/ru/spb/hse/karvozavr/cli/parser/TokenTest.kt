package ru.spb.hse.karvozavr.cli.parser

import org.junit.Test

import org.junit.Assert.*
import ru.spb.hse.karvozavr.cli.shell.env.CliEnvironment
import java.nio.file.Paths

class TokenTest {

    @Test
    fun testInterpolation() {
        val token = InterpolationToken("foo\$bar\$baz foo")
        val env = CliEnvironment(mutableMapOf("bar" to "1", "baz" to "2"))
        assertEquals("foo12 foo", token.interpolate(env).token)
    }

    @Test
    fun testNoInterpolation() {
        val token = InterpolationToken("foo foo")
        val env = CliEnvironment(mutableMapOf("bar" to "1", "baz" to "2"))
        assertEquals("foo foo", token.interpolate(env).token)
    }
}