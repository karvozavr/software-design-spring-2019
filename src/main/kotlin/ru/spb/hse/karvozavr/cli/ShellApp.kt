package ru.spb.hse.karvozavr.cli

import ru.spb.hse.karvozavr.cli.parser.CommandNode
import ru.spb.hse.karvozavr.cli.parser.CommandParser
import ru.spb.hse.karvozavr.cli.parser.ParseException
import ru.spb.hse.karvozavr.cli.pipeline.PipelineFactory
import ru.spb.hse.karvozavr.cli.shell.CliShell
import ru.spb.hse.karvozavr.cli.shell.Shell
import ru.spb.hse.karvozavr.cli.streams.ReadWriteStream
import ru.spb.hse.karvozavr.cli.streams.StdinStream
import ru.spb.hse.karvozavr.cli.streams.StdoutStream
import ru.spb.hse.karvozavr.cli.util.ExitCode

/**
 * Shell application logic.
 */
class ShellApp(val shell: Shell = CliShell.emptyShell()) {

    /**
     * Main loop of the shell.
     */
    fun mainLoop() {
        var commandLine: String?

        while (shell.isNotTerminated()) {
            print(shell.environment().shellPrompt())
            commandLine = readLine()

            if (commandLine != null) {
                var commands: List<CommandNode>
                try {
                    commands = CommandParser.parse(commandLine, shell.environment())
                } catch (e: ParseException) {
                    println("Parsing error: $e")
                    continue
                }

                val inputStream = StdinStream()
                val outputStream = StdoutStream()
                val errStream = ReadWriteStream()

                val pipeline = PipelineFactory.createPipeline(
                    commands,
                    inputStream,
                    outputStream,
                    errStream,
                    shell
                )

                shell.lastExitCode = pipeline.execute()
                if (shell.lastExitCode != ExitCode.SUCCESS)
                    while (errStream.isNotEmpty())
                        println(errStream.scanLine())
                shell.environment().variables()["?"] = shell.lastExitCode.code.toString()
            } else {
                break
            }
        }
    }
}