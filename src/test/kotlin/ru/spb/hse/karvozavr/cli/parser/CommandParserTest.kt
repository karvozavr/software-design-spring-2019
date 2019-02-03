package ru.spb.hse.karvozavr.cli.parser

import org.junit.Test

import org.junit.Assert.*
import ru.spb.hse.karvozavr.cli.shell.CliShell
import ru.spb.hse.karvozavr.cli.shell.env.CliEnvironment

class CommandParserTest {

    @Test
    fun parseCat1() {
        val env = CliEnvironment()
        assertEquals(
            CommandNode("cat", listOf("file.txt")),
            CommandParser.parse("cat file.txt", env).first()
        )
        println(CommandParser.parse("cat file.txt", env).first())
    }

    @Test
    fun parseEcho() {
        val env = CliEnvironment()
        val cmd = "echo foo bar    baz"
        val parsedValue = CommandParser.parse(cmd, env).first()
        assertEquals(
            CommandNode("echo", listOf("foo", "bar", "baz")),
            parsedValue
        )
        println(parsedValue)
    }

    @Test
    fun parseCat2() {
        val env = CliEnvironment()
        val cmd = "cat"
        val parsedValue = CommandParser.parse(cmd, env).first()
        assertEquals(
            CommandNode("cat", emptyList()),
            parsedValue
        )
        println(parsedValue)
    }

    @Test
    fun parseAssignment() {
        val env = CliEnvironment()
        val cmd = "foo=bar"
        val parsedValue = CommandParser.parse(cmd, env).first()
        assertEquals(
            CommandNode("=", listOf("foo", "bar")),
            parsedValue
        )
        println(parsedValue)
    }

    @Test
    fun parseAssignmentInterp() {
        val env = CliEnvironment(mutableMapOf("a" to "baz", "bbb" to "b", "ccc" to "c"))
        val cmd = "foo=\"b\$a \$bbb\$ccc r\""
        val parsedValue = CommandParser.parse(cmd, env).first()
        assertEquals(
            CommandNode("=", listOf("foo", "bbaz bc r")),
            parsedValue
        )
        println(parsedValue)
    }

    @Test
    fun parseEchoInterp() {
        val env = CliEnvironment(mutableMapOf("a" to "baz", "bbb" to "b", "ccc" to "c"))
        val cmd = "echo b\$a \$bbb\$ccc r"
        val parsedValue = CommandParser.parse(cmd, env).first()
        assertEquals(
            CommandNode("echo", listOf("bbaz", "bc", "r")),
            parsedValue
        )
        println(parsedValue)
    }

    @Test
    fun parsePipeline() {
        val env = CliEnvironment(mutableMapOf("a" to "baz", "bbb" to "b", "ccc" to "c"))
        val cmd = "echo b\$a \$bbb\$ccc r | cat | echo \$a\$c \'\$ccc\' | cat"
        val parsedValue = CommandParser.parse(cmd, env)
        assertArrayEquals(
            arrayOf(CommandNode("echo", listOf("bbaz", "bc", "r")),
                CommandNode("cat", listOf()),
                CommandNode("echo", listOf("baz", "\$ccc")),
                CommandNode("cat", listOf())),
            parsedValue.toTypedArray()
        )
        println(parsedValue)
    }
}