package ru.spb.hse.karvozavr.cli.util

/**
 * Error codes for shell commands.
 */
enum class ExitCode(val code: Int) {
    UNKNOWN_CODE(-1),
    SUCCESS(0),
    INVALID_ARGUMENTS(1),
    RESOURCE_NOT_FOUND(2),
    UNKNOWN_COMMAND(3)
}