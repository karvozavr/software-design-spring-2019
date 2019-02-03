package ru.spb.hse.karvozavr.cli.pipeline

import ru.spb.hse.karvozavr.cli.util.ExitCode

/**
 * Command pipeline entity.
 */
interface Pipeline {

    /**
     * Executes this pipeline.
     *
     * @return exit code of the last executed command
     */
    fun execute(): ExitCode
}
