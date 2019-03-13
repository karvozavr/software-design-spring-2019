package ru.spb.hse.karvozavr.cli.commands.builtins

import ru.spb.hse.karvozavr.cli.commands.Command
import ru.spb.hse.karvozavr.cli.shell.Shell
import ru.spb.hse.karvozavr.cli.streams.InStream
import ru.spb.hse.karvozavr.cli.streams.OutStream
import ru.spb.hse.karvozavr.cli.util.ExitCode
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import kotlin.system.exitProcess

/**
 * Find pattern in input stream.
 */
class GrepCommand(
    args: List<String>,
    inputStream: InStream,
    outStream: OutStream,
    errStream: OutStream,
    shell: Shell
) : Command(args, inputStream, outStream, errStream, shell) {

    private class GrepArgs(parser: ArgParser) {
        val caseInsensitive by parser.flagging(
            "-i",
            help = "search case-insensitive"
        )

        val word by parser.flagging(
            "-w",
            help = "look for the whole word match"
        )

        val count by parser.storing(
            "-A",
            help = "print n lines after match"
        ) { toInt() }.default(0)

        val pattern by parser.positional(
            "PATTERN",
            help = "pattern to search for"
        )
    }

    override fun execute(): ExitCode {
        try {
            ArgParser(args = args.toTypedArray()).parseInto(::GrepArgs).run {
                val pattern = if (word) "(\\s+|^)$pattern(\\s+|$)" else pattern
                val options = mutableSetOf<RegexOption>()

                if (caseInsensitive)
                    options.add(RegexOption.IGNORE_CASE)

                val r = pattern.toRegex(options)

                var printLines = 0

                while (inputStream.isNotEmpty()) {
                    val line = inputStream.scanLine()
                    if (r.containsMatchIn(line)) {
                        writeLine(line)
                        printLines = count
                    } else if (printLines > 0) {
                        writeLine(line)
                        --printLines
                    }
                }
            }
        } catch (e: Throwable) {
            return ExitCode.INVALID_ARGUMENTS
        }

        return ExitCode.SUCCESS
    }
}