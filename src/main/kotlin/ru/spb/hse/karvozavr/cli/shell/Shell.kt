package ru.spb.hse.karvozavr.cli.shell

import ru.spb.hse.karvozavr.cli.shell.env.Environment
import ru.spb.hse.karvozavr.cli.util.ExitCode

/**
 * Shell interface.
 */
interface Shell {

    /**
     * Exit code of the last command.
     */
    var lastExitCode: ExitCode

    /**
     * Returns environment of this shell.
     *
     * @return environment of this shell
     */
    fun environment(): Environment

    /**
     * Returns if this shell is terminated.
     *
     * @return if this shell is terminated
     */
    fun isTerminated(): Boolean

    /**
     * Returns if this shell is not terminated.
     *
     * @return if this shell is not terminated
     */
    fun isNotTerminated() = !isTerminated()

    /**
     *
     */
    fun terminate()
}
