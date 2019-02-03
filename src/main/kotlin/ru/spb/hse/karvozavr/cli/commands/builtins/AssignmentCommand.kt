package ru.spb.hse.karvozavr.cli.commands.builtins

import ru.spb.hse.karvozavr.cli.commands.Command
import ru.spb.hse.karvozavr.cli.shell.Shell
import ru.spb.hse.karvozavr.cli.streams.InStream
import ru.spb.hse.karvozavr.cli.streams.OutStream
import ru.spb.hse.karvozavr.cli.util.ExitCode

/**
 * Assignment command.
 *
 * Example:
 *  foo=bar
 */
class AssignmentCommand(
    args: List<String>,
    inputStream: InStream,
    outStream: OutStream,
    errStream: OutStream,
    shell: Shell
) : Command(args, inputStream, outStream, errStream, shell) {

    override fun execute(): ExitCode {
        if (args.size != 2) {
            return ExitCode.INVALID_ARGUMENTS
        }
        shell().environment().variables()[args[0]] = args[1]
        return ExitCode.SUCCESS
    }
}