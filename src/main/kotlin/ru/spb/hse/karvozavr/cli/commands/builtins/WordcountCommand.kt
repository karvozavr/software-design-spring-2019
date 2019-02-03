package ru.spb.hse.karvozavr.cli.commands.builtins

import ru.spb.hse.karvozavr.cli.commands.Command
import ru.spb.hse.karvozavr.cli.shell.Shell
import ru.spb.hse.karvozavr.cli.streams.InStream
import ru.spb.hse.karvozavr.cli.streams.OutStream
import ru.spb.hse.karvozavr.cli.util.ExitCode
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Wordcount command.
 */
class WordcountCommand(
    args: List<String>,
    inputStream: InStream,
    outStream: OutStream,
    errStream: OutStream,
    shell: Shell
) : Command(args, inputStream, outStream, errStream, shell) {

    override fun execute(): ExitCode = when (args.size) {
        0 -> wcStdin()
        1 -> wcFile(args.first())
        else -> {
            writeError("Invalid arguments: cat command require 1 or 0 arguments.")
            ExitCode.INVALID_ARGUMENTS
        }
    }

    private fun outputResult(lines: Long, words: Long, bytes: Long) {
        writeLine("$lines $words $bytes")
    }

    private fun wcStdin(): ExitCode {
        var lines = 0L
        var words = 0L
        var bytes = 0L
        while (inputStream.isNotEmpty()) {
            val line = inputStream.scanLine()
            lines++
            words += line.split("\\s+".toRegex()).size
            bytes += line.length + 1
        }
        outputResult(lines, words, bytes)
        return ExitCode.SUCCESS
    }

    private fun wcFile(file: String): ExitCode {
        try {
            val path = Paths.get(file)

            var lines = 0L
            var words = 0L
            val bytes = Files.size(path)

            Files.lines(path).forEach {
                lines++
                if (it.isNotEmpty())
                    words += it.split("\\s+".toRegex()).size
            }

            outputResult(lines, words, bytes)
        } catch (e: FileNotFoundException) {
            writeError("File $file doesn't exist in a filesystem.")
            return ExitCode.RESOURCE_NOT_FOUND
        }
        return ExitCode.SUCCESS
    }
}