package ru.spb.hse.karvozavr.cli.commands.builtins

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.spb.hse.karvozavr.cli.shell.CliShell
import ru.spb.hse.karvozavr.cli.streams.EmptyStream
import ru.spb.hse.karvozavr.cli.streams.ReadWriteStream
import ru.spb.hse.karvozavr.cli.util.ExitCode
import java.nio.file.Paths

class WordcountCommandTest {

    private val testDirecory = Paths.get("src", "test", "resources").toAbsolutePath()
    private val file1 = (testDirecory.resolve("file1.txt")).toString()
    private val emptyFile = (testDirecory.resolve("empty.txt")).toString()

    @Test
    fun testEmpty() {
        val errStream = ReadWriteStream()
        val outStream = ReadWriteStream()

        val cmd = WordcountCommand(listOf(emptyFile), EmptyStream, outStream, errStream, CliShell.emptyShell())

        assertEquals(ExitCode.SUCCESS, cmd.execute())
        assertEquals(true, outStream.isNotEmpty())
        assertEquals("0 0 0", outStream.scanLine())
        assertEquals(true, outStream.isEmpty())
        assertEquals(true, errStream.isEmpty())
    }

    @Test
    fun testFile() {
        val errStream = ReadWriteStream()
        val outStream = ReadWriteStream()

        val cmd = WordcountCommand(listOf(file1), EmptyStream, outStream, errStream, CliShell.emptyShell())

        assertEquals(ExitCode.SUCCESS, cmd.execute())
        assertEquals(true, outStream.isNotEmpty())
        assertEquals("7 9 48", outStream.scanLine())
        assertEquals(true, outStream.isEmpty())
        assertEquals(true, errStream.isEmpty())
    }
}