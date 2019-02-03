package ru.spb.hse.karvozavr.cli.commands.builtins

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.spb.hse.karvozavr.cli.shell.CliShell
import ru.spb.hse.karvozavr.cli.streams.EmptyStream
import ru.spb.hse.karvozavr.cli.streams.ReadWriteStream
import ru.spb.hse.karvozavr.cli.util.ExitCode

class EchoCommandTest {

    @Test
    fun testEmpty() {
        val errStream = ReadWriteStream()
        val outStream = ReadWriteStream()

        val cmd = EchoCommand(
            emptyList(),
            EmptyStream,
            outStream,
            errStream,
            CliShell.emptyShell()
        )

        assertEquals(ExitCode.SUCCESS, cmd.execute())
        assertEquals(true, outStream.isNotEmpty())
        assertEquals("", outStream.scanLine())
        assertEquals(true, outStream.isEmpty())
        assertEquals(true, errStream.isEmpty())
    }

    @Test
    fun testOne() {
        val errStream = ReadWriteStream()
        val outStream = ReadWriteStream()

        val cmd = EchoCommand(
            listOf("arg"),
            EmptyStream,
            outStream,
            errStream,
            CliShell.emptyShell()
        )

        assertEquals(ExitCode.SUCCESS, cmd.execute())
        assertEquals(true, outStream.isNotEmpty())
        assertEquals("arg", outStream.scanLine())
        assertEquals(true, outStream.isEmpty())
        assertEquals(true, errStream.isEmpty())
    }

    @Test
    fun testMany() {
        val errStream = ReadWriteStream()
        val outStream = ReadWriteStream()

        val data = listOf("arg1", "arg2", "arg3", "arg4")
        val cmd = EchoCommand(
            data,
            EmptyStream,
            outStream,
            errStream,
            CliShell.emptyShell()
        )

        assertEquals(ExitCode.SUCCESS, cmd.execute())
        assertEquals(true, outStream.isNotEmpty())
        assertEquals(data.joinToString(separator = " "), outStream.scanLine())
        assertEquals(true, outStream.isEmpty())
        assertEquals(true, errStream.isEmpty())
    }
}
