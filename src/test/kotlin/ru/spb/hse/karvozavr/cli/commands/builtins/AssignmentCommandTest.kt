package ru.spb.hse.karvozavr.cli.commands.builtins

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.spb.hse.karvozavr.cli.shell.CliShell
import ru.spb.hse.karvozavr.cli.streams.EmptyStream
import ru.spb.hse.karvozavr.cli.streams.ReadWriteStream
import ru.spb.hse.karvozavr.cli.util.ExitCode

class AssignmentCommandTest {

    @Test
    fun testAssignment() {
        val errStream = ReadWriteStream()
        val outStream = ReadWriteStream()
        val shell = CliShell.emptyShell()

        val cmd = AssignmentCommand(
            listOf("foo", "bar"),
            EmptyStream,
            outStream,
            errStream,
            shell
        )

        assertEquals(ExitCode.SUCCESS, cmd.execute())
        assertEquals(true, outStream.isEmpty())
        assertEquals(true, errStream.isEmpty())
        assertEquals("bar", shell.environment().variables()["foo"])
    }

    @Test
    fun testModifying() {
        val errStream = ReadWriteStream()
        val outStream = ReadWriteStream()
        val shell = CliShell.emptyShell()
        shell.environment().variables()["foo"] = "baz"

        val cmd = AssignmentCommand(
            listOf("foo", "bar"),
            EmptyStream,
            outStream,
            errStream,
            shell
        )

        assertEquals(ExitCode.SUCCESS, cmd.execute())
        assertEquals(true, outStream.isEmpty())
        assertEquals(true, errStream.isEmpty())
        assertEquals("bar", shell.environment().variables()["foo"])
    }
}