package ru.spb.hse.karvozavr.cli.commands.builtins

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.spb.hse.karvozavr.cli.shell.CliShell
import ru.spb.hse.karvozavr.cli.streams.EmptyStream
import ru.spb.hse.karvozavr.cli.streams.ReadWriteStream
import ru.spb.hse.karvozavr.cli.util.ExitCode
import java.nio.file.Files
import java.nio.file.Paths


class CatCommandTest {

    private val testDirecory = Paths.get("src", "test", "resources").toAbsolutePath()
    private val file = (testDirecory.resolve("file1.txt")).toString()

    @Test
    fun testCatFile() {
        val errStream = ReadWriteStream()
        val outStream = ReadWriteStream()

        val cmd = CatCommand(
            listOf(file),
            EmptyStream,
            outStream,
            errStream,
            CliShell.emptyShell()
        )

        assertEquals(ExitCode.SUCCESS, cmd.execute())
        assertEquals(true, outStream.isNotEmpty())
        assertEquals(true, errStream.isEmpty())

        Files.lines(Paths.get(file))
            .forEach { assertEquals(it, outStream.scanLine()) }

        assertEquals(true, outStream.isEmpty())
    }

    @Test
    fun testCatStdin() {
        val inStream = ReadWriteStream()
        val errStream = ReadWriteStream()
        val inputData = listOf("Hello", "World")
        inputData.forEach { inStream.write(it) }
        val outStream = ReadWriteStream()

        val cmd = CatCommand(emptyList(), inStream, outStream, errStream, CliShell.emptyShell())

        assertEquals(ExitCode.SUCCESS, cmd.execute())
        assertEquals(true, outStream.isNotEmpty())
        assertEquals(true, errStream.isEmpty())

        val seq = generateSequence { if (outStream.isNotEmpty()) outStream.scanLine() else null }
        assertEquals(inputData, seq.toList())
        assertEquals(true, outStream.isEmpty())
    }
}
