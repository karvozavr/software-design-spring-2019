package ru.spb.hse.karvozavr.cli

import ru.spb.hse.karvozavr.cli.shell.CliShell
import ru.spb.hse.karvozavr.cli.shell.env.CliEnvironment
import java.nio.file.Paths

/**
 * Main function.
 */
fun main(args: Array<String>) {
    val app = if (args.isNotEmpty())
        ShellApp(CliShell(CliEnvironment(currentDir = Paths.get(args[0]))))
    else
        ShellApp()

    app.mainLoop()
}
