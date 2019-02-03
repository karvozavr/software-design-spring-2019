package ru.spb.hse.karvozavr.cli.pipeline

import org.junit.Assert.*
import org.junit.Test
import ru.spb.hse.karvozavr.cli.commands.builtins.CatCommand
import ru.spb.hse.karvozavr.cli.commands.builtins.EchoCommand
import ru.spb.hse.karvozavr.cli.shell.CliShell
import ru.spb.hse.karvozavr.cli.streams.EmptyStream
import ru.spb.hse.karvozavr.cli.streams.ReadWriteStream
import ru.spb.hse.karvozavr.cli.util.ExitCode
import java.nio.file.Files
import java.nio.file.Paths

class SingleCommandPipelineTest {
    private val testDirecory = Paths.get("src", "test", "resources").toAbsolutePath()
    private val file = (testDirecory.resolve("file1.txt")).toString()

    @Test
    fun testCatFile() {
        val errStream = ReadWriteStream()
        val outStream = ReadWriteStream()

        val cmd = SingleCommandPipeline(
            CatCommand(
                listOf(file),
                EmptyStream,
                outStream,
                errStream,
                CliShell.emptyShell()
            )
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

        val cmd = SingleCommandPipeline(CatCommand(emptyList(), inStream, outStream, errStream, CliShell.emptyShell()))

        assertEquals(ExitCode.SUCCESS, cmd.execute())
        assertEquals(true, outStream.isNotEmpty())
        assertEquals(true, errStream.isEmpty())

        val seq = generateSequence { if (outStream.isNotEmpty()) outStream.scanLine() else null }
        assertEquals(inputData, seq.toList())
        assertEquals(true, outStream.isEmpty())
    }

    @Test
    fun testEmpty() {
        val errStream = ReadWriteStream()
        val outStream = ReadWriteStream()

        val cmd = SingleCommandPipeline(
            EchoCommand(
                emptyList(),
                EmptyStream,
                outStream,
                errStream,
                CliShell.emptyShell()
            )
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

        val cmd = SingleCommandPipeline(
            EchoCommand(
                listOf("arg"),
                EmptyStream,
                outStream,
                errStream,
                CliShell.emptyShell()
            )
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
        val cmd = SingleCommandPipeline(
            EchoCommand(
                data,
                EmptyStream,
                outStream,
                errStream,
                CliShell.emptyShell()
            )
        )

        assertEquals(ExitCode.SUCCESS, cmd.execute())
        assertEquals(true, outStream.isNotEmpty())
        assertEquals(data.joinToString(separator = " "), outStream.scanLine())
        assertEquals(true, outStream.isEmpty())
        assertEquals(true, errStream.isEmpty())
    }
}