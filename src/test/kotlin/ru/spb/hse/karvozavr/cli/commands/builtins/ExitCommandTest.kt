package ru.spb.hse.karvozavr.cli.commands.builtins

import org.junit.Assert.*
import org.junit.Test
import ru.spb.hse.karvozavr.cli.shell.CliShell
import ru.spb.hse.karvozavr.cli.streams.EmptyStream
import ru.spb.hse.karvozavr.cli.streams.ReadWriteStream
import ru.spb.hse.karvozavr.cli.util.ExitCode

class ExitCommandTest {

    @Test
    fun testEmpty() {
        val errStream = ReadWriteStream()
        val outStream = ReadWriteStream()
        val shell = CliShell.emptyShell()

        val cmd = ExitCommand(
            emptyList(),
            EmptyStream,
            outStream,
            errStream,
            shell
        )

        assertEquals(ExitCode.SUCCESS, cmd.execute())
        assertEquals(true, outStream.isEmpty())
        assertEquals(true, shell.isTerminated())
    }
}