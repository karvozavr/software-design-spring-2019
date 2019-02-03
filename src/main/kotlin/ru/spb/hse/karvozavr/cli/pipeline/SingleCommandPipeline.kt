package ru.spb.hse.karvozavr.cli.pipeline

import ru.spb.hse.karvozavr.cli.commands.Command
import ru.spb.hse.karvozavr.cli.util.ExitCode

/**
 * Pipeline of a single command.
 */
class SingleCommandPipeline(private val command: Command) : Pipeline {
    override fun execute(): ExitCode =
        command.execute()
}