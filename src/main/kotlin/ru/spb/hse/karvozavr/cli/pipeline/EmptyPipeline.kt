package ru.spb.hse.karvozavr.cli.pipeline

import ru.spb.hse.karvozavr.cli.util.ExitCode

/**
 * Pipeline of 0 commands, exists for generalisation purpose.
 */
object EmptyPipeline : Pipeline {
    override fun execute(): ExitCode =
        ExitCode.SUCCESS
}