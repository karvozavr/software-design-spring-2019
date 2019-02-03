package ru.spb.hse.karvozavr.cli.commands.builtins

import org.junit.Test

import org.junit.Assert.*
import ru.spb.hse.karvozavr.cli.shell.CliShell
import ru.spb.hse.karvozavr.cli.streams.EmptyStream
import ru.spb.hse.karvozavr.cli.streams.ReadWriteStream
import ru.spb.hse.karvozavr.cli.util.ExitCode

class ExternalCommandTest {

    @Test
    fun testArgs() {
        val errStream = ReadWriteStream()
        val outStream = ReadWriteStream()

        val cmd = ExternalCommand(
            listOf("-lha"),
            EmptyStream,
            outStream,
            errStream,
            CliShell.emptyShell(),
            "ls",
            true
        )

        assertEquals(ExitCode.SUCCESS, cmd.execute())
        assertEquals(true, outStream.isNotEmpty())
    }

    @Test
    fun testStdin() {
        val errStream = ReadWriteStream()
        val outStream = ReadWriteStream()
        val inputStream = ReadWriteStream()
        val l1 = "Hello, World\n";
        val l2 = "I'm here"
        inputStream.write(l1)
        inputStream.write(l2)

        val cmd = ExternalCommand(
            emptyList(),
            inputStream,
            outStream,
            errStream,
            CliShell.emptyShell(),
            "cat",
            true
        )

        assertEquals(ExitCode.SUCCESS, cmd.execute())
        assertEquals(l1.trim(), outStream.scanLine())
        assertEquals(l2.trim(), outStream.scanLine())
        assertEquals(true, outStream.isEmpty())
    }
}