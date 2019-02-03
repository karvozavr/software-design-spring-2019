package ru.spb.hse.karvozavr.cli.commands

import ru.spb.hse.karvozavr.cli.shell.Shell
import ru.spb.hse.karvozavr.cli.streams.InStream
import ru.spb.hse.karvozavr.cli.streams.OutStream
import ru.spb.hse.karvozavr.cli.util.ExitCode

/**
 * Command base interface.
 *
 * @param args command line arguments of this command
 * @param inputStream input of this command
 * @param outStream output of this command
 * @param errStream error output of this command
 * @param shell shell context the command being executed in
 */
abstract class Command(
    protected val args: List<String>,
    protected val inputStream: InStream,
    private val outStream: OutStream,
    private val errStream: OutStream,
    private val shell: Shell
) {
    /**
     * Writes error line to error output.
     *
     * @param err error line to output
     */
    protected fun writeError(err: String) {
        errStream.write(err)
    }

    /**
     * Writes line to output.
     *
     * @param line line to output
     */
    protected fun writeLine(line: String) {
        outStream.write(line)
    }

    /**
     * Access shell context of this command.
     *
     * @return shell context
     */
    protected fun shell(): Shell = shell

    /**
     * Execute command in it's context using IO streams from constructor params.
     *
     * @return exit code of the command
     */
    abstract fun execute(): ExitCode
}
