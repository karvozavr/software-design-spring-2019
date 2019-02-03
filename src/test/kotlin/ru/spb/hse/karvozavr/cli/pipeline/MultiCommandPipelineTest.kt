package ru.spb.hse.karvozavr.cli.pipeline

import org.junit.Test

import org.junit.Assert.*
import ru.spb.hse.karvozavr.cli.parser.CommandNode
import ru.spb.hse.karvozavr.cli.shell.CliShell
import ru.spb.hse.karvozavr.cli.streams.EmptyStream
import ru.spb.hse.karvozavr.cli.streams.ReadWriteStream
import ru.spb.hse.karvozavr.cli.util.ExitCode
import java.nio.file.Files
import java.nio.file.Paths

class MultiCommandPipelineTest {

    private val testDirecory = Paths.get("src", "test", "resources").toAbsolutePath()
    private val file = (testDirecory.resolve("file1.txt")).toString()

    @Test
    fun testCatToCat() {
        val errStream = ReadWriteStream()
        val outStream = ReadWriteStream()
        val pipeline: Pipeline = PipelineFactory.createPipeline(
            listOf(
                CommandNode("cat", listOf(file)),
                CommandNode("cat", emptyList())
            ),
            EmptyStream,
            outStream,
            errStream,
            CliShell.emptyShell()
        )

        assertEquals(ExitCode.SUCCESS, pipeline.execute())
        assertEquals(true, outStream.isNotEmpty())
        assertEquals(true, errStream.isEmpty())

        Files.lines(Paths.get(file))
            .forEach { assertEquals(it, outStream.scanLine()) }

        assertEquals(true, outStream.isEmpty())
    }

    @Test
    fun testLong() {
        val errStream = ReadWriteStream()
        val outStream = ReadWriteStream()
        val pipeline: Pipeline = PipelineFactory.createPipeline(
            listOf(
                CommandNode("cat", listOf(file)),
                CommandNode("cat", emptyList()),
                CommandNode("cat", emptyList()),
                CommandNode("cat", emptyList()),
                CommandNode("cat", emptyList())
            ),
            EmptyStream,
            outStream,
            errStream,
            CliShell.emptyShell()
        )

        assertEquals(ExitCode.SUCCESS, pipeline.execute())
        assertEquals(true, outStream.isNotEmpty())
        assertEquals(true, errStream.isEmpty())

        Files.lines(Paths.get(file))
            .forEach { assertEquals(it, outStream.scanLine()) }

        assertEquals(true, outStream.isEmpty())
    }

    @Test
    fun testEchoToCat() {
        val errStream = ReadWriteStream()
        val outStream = ReadWriteStream()
        val pipeline: Pipeline = PipelineFactory.createPipeline(
            listOf(
                CommandNode("echo", listOf("foo", "bar")),
                CommandNode("cat", emptyList())
            ),
            EmptyStream,
            outStream,
            errStream,
            CliShell.emptyShell()
        )

        assertEquals(ExitCode.SUCCESS, pipeline.execute())
        assertEquals(true, outStream.isNotEmpty())
        assertEquals(true, errStream.isEmpty())
        assertEquals("foo bar", outStream.scanLine())
        assertEquals(true, outStream.isEmpty())
    }


}