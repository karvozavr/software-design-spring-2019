package ru.spb.hse.karvozavr.cli.commands.builtins

import org.junit.Assert.*
import org.junit.Test
import ru.spb.hse.karvozavr.cli.shell.CliShell
import ru.spb.hse.karvozavr.cli.streams.EmptyStream
import ru.spb.hse.karvozavr.cli.streams.ReadWriteStream
import ru.spb.hse.karvozavr.cli.util.ExitCode

class PwdCommandTest {

    @Test
    fun testEmpty() {
        val errStream = ReadWriteStream()
        val outStream = ReadWriteStream()

        val shell = CliShell.emptyShell()
        val dir = shell.environment().currentDir().toString()

        val cmd = PwdCommand(emptyList(), EmptyStream, outStream, errStream, CliShell.emptyShell())

        assertEquals(ExitCode.SUCCESS, cmd.execute())
        assertEquals(true, outStream.isNotEmpty())
        assertEquals(dir, outStream.scanLine())
        assertEquals(true, outStream.isEmpty())
        assertEquals(true, errStream.isEmpty())
    }
}