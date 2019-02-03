package ru.spb.hse.karvozavr.cli.shell.env

import java.nio.file.Path

/**
 * Environment of a shell.
 */
interface Environment {

    /**
     * Returns environment variables.
     *
     * @return environment vars
     */
    fun variables(): MutableMap<String, String>

    /**
     * Returns current working directory
     *
     * @return current working directory
     */
    fun currentDir(): Path

    /**
     * Changes current working directiry to `newDir`.
     *
     * @param newDir new working directory
     */
    fun changeDirectory(newDir: Path)

    /**
     * Return shell prompt.
     *
     * @return shell prompt
     */
    fun shellPrompt(): String
}

