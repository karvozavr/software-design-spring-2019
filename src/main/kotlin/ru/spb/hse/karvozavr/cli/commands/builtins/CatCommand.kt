package ru.spb.hse.karvozavr.cli.commands.builtins

import ru.spb.hse.karvozavr.cli.commands.Command
import ru.spb.hse.karvozavr.cli.shell.Shell
import ru.spb.hse.karvozavr.cli.streams.InStream
import ru.spb.hse.karvozavr.cli.streams.OutStream
import ru.spb.hse.karvozavr.cli.util.ExitCode
import java.io.FileNotFoundException
import java.io.FileReader

/**
 * Cat command.
 */
class CatCommand(
    args: List<String>,
    inputStream: InStream,
    outStream: OutStream,
    errStream: OutStream,
    shell: Shell
) : Command(args, inputStream, outStream, errStream, shell) {

    override fun execute(): ExitCode = when (args.size) {
        0 -> catStdin()
        1 -> catFile(args.first())
        else -> {
            writeError("Invalid arguments: cat command require 1 or 0 arguments.")
            ExitCode.INVALID_ARGUMENTS
        }
    }

    private fun catStdin(): ExitCode {
        while (inputStream.isNotEmpty())
            writeLine(inputStream.scanLine())
        return ExitCode.SUCCESS
    }

    private fun catFile(file: String): ExitCode {
        try {
            FileReader(file).use {
                it.readLines().forEach { line -> writeLine(line) }
            }
        } catch (e: FileNotFoundException) {
            writeError("File $file doesn't exist in a filesystem.")
            return ExitCode.RESOURCE_NOT_FOUND
        }
        return ExitCode.SUCCESS
    }
}
