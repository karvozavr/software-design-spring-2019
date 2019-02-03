package ru.spb.hse.karvozavr.cli.commands.builtins

import ru.spb.hse.karvozavr.cli.commands.Command
import ru.spb.hse.karvozavr.cli.shell.Shell
import ru.spb.hse.karvozavr.cli.streams.InStream
import ru.spb.hse.karvozavr.cli.streams.OutStream
import ru.spb.hse.karvozavr.cli.util.ExitCode
import java.io.FileNotFoundException
import java.nio.file.Paths

/**
 * Change directory command (cd).
 */
class ChangeDirectoryCommand(
    args: List<String>,
    inputStream: InStream,
    outStream: OutStream,
    errStream: OutStream,
    shell: Shell
) : Command(args, inputStream, outStream, errStream, shell) {

    override fun execute(): ExitCode {
        if (args.isNotEmpty()) {
            return try {
                val newPath = shell().environment().currentDir().resolve(Paths.get(args[0]))
                shell().environment().changeDirectory(newPath)
                ExitCode.SUCCESS
            } catch (e: FileNotFoundException) {
                ExitCode.RESOURCE_NOT_FOUND
            }
        } else {
            writeError("This command accepts only 1 argument!")
            return ExitCode.INVALID_ARGUMENTS
        }
    }
}