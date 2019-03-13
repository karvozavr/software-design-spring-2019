package ru.spb.hse.karvozavr.cli.commands.builtins

import org.junit.Test

import org.junit.Assert.*
import ru.spb.hse.karvozavr.cli.shell.CliShell
import ru.spb.hse.karvozavr.cli.streams.ReadWriteStream
import ru.spb.hse.karvozavr.cli.util.ExitCode

class GrepCommandTest {

    @Test
    fun testSimple() {
        val inStream = ReadWriteStream()
        val errStream = ReadWriteStream()
        val inputData = listOf("big brown Fox", "jumps over", "the lazy fox")
        inputData.forEach { inStream.write(it) }
        val outStream = ReadWriteStream()

        val cmd = GrepCommand(listOf("fox"), inStream, outStream, errStream, CliShell.emptyShell())

        assertEquals(ExitCode.SUCCESS, cmd.execute())
        assertEquals(true, outStream.isNotEmpty())
        assertEquals(true, errStream.isEmpty())

        val seq = generateSequence { if (outStream.isNotEmpty()) outStream.scanLine() else null }
        assertEquals(listOf("the lazy fox"), seq.toList())
        assertEquals(true, outStream.isEmpty())
    }

    @Test
    fun testEmptyArgs() {
        val inStream = ReadWriteStream()
        val errStream = ReadWriteStream()
        val inputData = listOf("big brown fox", "jumps over", "the lazy fox")
        inputData.forEach { inStream.write(it) }
        val outStream = ReadWriteStream()

        val cmd = GrepCommand(listOf(), inStream, outStream, errStream, CliShell.emptyShell())

        assertEquals(ExitCode.INVALID_ARGUMENTS, cmd.execute())
    }

    @Test
    fun testIgnoreCase() {
        val inStream = ReadWriteStream()
        val errStream = ReadWriteStream()
        val inputData = listOf("big brown Fox", "jumps over", "the lazy fox")
        inputData.forEach { inStream.write(it) }
        val outStream = ReadWriteStream()

        val cmd = GrepCommand(listOf("fox", "-i"), inStream, outStream, errStream, CliShell.emptyShell())

        assertEquals(ExitCode.SUCCESS, cmd.execute())
        assertEquals(true, outStream.isNotEmpty())
        assertEquals(true, errStream.isEmpty())

        val seq = generateSequence { if (outStream.isNotEmpty()) outStream.scanLine() else null }
        assertEquals(listOf("big brown Fox", "the lazy fox"), seq.toList())
        assertEquals(true, outStream.isEmpty())
    }

    @Test
    fun testWord() {
        val inStream = ReadWriteStream()
        val errStream = ReadWriteStream()
        val inputData = listOf("big brown fox", "jumps over", "the lazy fox-terrier")
        inputData.forEach { inStream.write(it) }
        val outStream = ReadWriteStream()

        val cmd = GrepCommand(listOf("fox", "-w"), inStream, outStream, errStream, CliShell.emptyShell())

        assertEquals(ExitCode.SUCCESS, cmd.execute())
        assertEquals(true, outStream.isNotEmpty())
        assertEquals(true, errStream.isEmpty())

        val seq = generateSequence { if (outStream.isNotEmpty()) outStream.scanLine() else null }
        assertEquals(listOf("big brown fox"), seq.toList())
        assertEquals(true, outStream.isEmpty())
    }

    @Test
    fun testNLines() {
        val inStream = ReadWriteStream()
        val errStream = ReadWriteStream()
        val inputData = listOf("big brown Fox", "jumps over the fence", "the lazy fox-terrier", "really lazy", "one")
        inputData.forEach { inStream.write(it) }
        val outStream = ReadWriteStream()

        val cmd = GrepCommand(listOf("fence", "-A", "2"), inStream, outStream, errStream, CliShell.emptyShell())

        assertEquals(ExitCode.SUCCESS, cmd.execute())
        assertEquals(true, outStream.isNotEmpty())
        assertEquals(true, errStream.isEmpty())

        val seq = generateSequence { if (outStream.isNotEmpty()) outStream.scanLine() else null }
        assertEquals(listOf("jumps over the fence", "the lazy fox-terrier", "really lazy"), seq.toList())
        assertEquals(true, outStream.isEmpty())
    }
}