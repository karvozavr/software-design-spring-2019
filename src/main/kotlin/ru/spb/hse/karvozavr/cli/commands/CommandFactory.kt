package ru.spb.hse.karvozavr.cli.commands

import ru.spb.hse.karvozavr.cli.commands.builtins.*
import ru.spb.hse.karvozavr.cli.shell.Shell
import ru.spb.hse.karvozavr.cli.streams.InStream
import ru.spb.hse.karvozavr.cli.streams.OutStream

/**
 * Factory responsible for creation of command entity by it's name and parameters.
 */
object CommandFactory {

    /**
     * Creates executable command.
     *
     * @param commandName name of a command
     * @param args arguments of a command
     * @param inStream input stream
     * @param outStream output stream
     * @param errStream error stream
     * @param shell shell environment
     * @param inPipeline if the command in pipeline or not
     *
     * @return executable pipeline
     */
    fun createCommand(
        commandName: String,
        args: List<String>,
        inStream: InStream,
        outStream: OutStream,
        errStream: OutStream,
        shell: Shell,
        inPipeline: Boolean
    ): Command = when (commandName) {
        "cat" -> CatCommand(args, inStream, outStream, errStream, shell)
        "echo" -> EchoCommand(args, inStream, outStream, errStream, shell)
        "wc" -> WordcountCommand(args, inStream, outStream, errStream, shell)
        "pwd" -> PwdCommand(args, inStream, outStream, errStream, shell)
        "exit" -> ExitCommand(args, inStream, outStream, errStream, shell)
        "=" -> AssignmentCommand(args, inStream, outStream, errStream, shell)
        else -> ExternalCommand(
            args,
            inStream,
            outStream,
            errStream,
            shell,
            commandName,
            inPipeline
        )
    }
}