package ru.spb.hse.karvozavr.cli.shell

import ru.spb.hse.karvozavr.cli.shell.env.CliEnvironment
import ru.spb.hse.karvozavr.cli.shell.env.Environment
import ru.spb.hse.karvozavr.cli.util.ExitCode
import java.nio.file.Paths

/**
 * Basic shell implementation.
 */
class CliShell(val environment: Environment) : Shell {

    companion object {
        /**
         * Create basic shell with current dir in the directory context of current process.
         */
        fun emptyShell(): CliShell = CliShell(
            CliEnvironment(currentDir = Paths.get(System.getProperty("user.dir")))
        )
    }

    override var lastExitCode: ExitCode = ExitCode.SUCCESS

    private var terminated: Boolean = false

    override fun environment(): Environment =
        environment

    override fun isTerminated(): Boolean =
        terminated

    override fun terminate() {
        terminated = true
    }
}