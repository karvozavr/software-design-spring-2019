package ru.spb.hse.karvozavr.cli.shell.env

import java.nio.file.Path
import java.nio.file.Paths

/**
 * Basic shell environment implementation.
 */
class CliEnvironment(
    private val variables: MutableMap<String, String> = mutableMapOf(),
    private var currentDir: Path = Paths.get("/")
) : Environment {

    override fun variables(): MutableMap<String, String> =
        variables

    override fun currentDir(): Path =
        currentDir

    override fun changeDirectory(newDir: Path) {
        currentDir = Paths.get(newDir.toString())
    }

    override fun shellPrompt(): String {
        return "$currentDir$ "
    }
}