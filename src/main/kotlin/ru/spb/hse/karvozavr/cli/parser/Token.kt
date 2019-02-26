package ru.spb.hse.karvozavr.cli.parser

import ru.spb.hse.karvozavr.cli.shell.env.Environment
import java.lang.StringBuilder

/**
 * Lexer token.
 */
sealed class Token(val token: String) {

    /**
     * Interpolate this token in context of given environment.
     */
    abstract fun interpolate(env: Environment): Token
}

/**
 * Token which require interpolation.
 */
class InterpolationToken(token: String) : Token(token) {
    override fun interpolate(env: Environment): NoInterpolationToken {
        val r: Regex = Regex("[$]([A-Za-z0-9_]+|[?])")
        val valuesList = mutableListOf<String>()

        for (result in r.findAll(token)) {
            valuesList.add(env.variables()[result.value.substring(1)] ?: "")
        }

        val pieces = token.split(r)
        val stringBuilder = StringBuilder()

        for (i in 0 until pieces.size) {
            stringBuilder.append(pieces[i])
            if (i < valuesList.size)
                stringBuilder.append(valuesList[i])
        }

        return ValueToken(stringBuilder.toString())
    }
}

/**
 * Token which doesn't require interpolation.
 */
sealed class NoInterpolationToken(token: String) : Token(token) {
    override fun interpolate(env: Environment): NoInterpolationToken {
        return this
    }
}

/**
 * Pipe token.
 */
object PipeToken : NoInterpolationToken("|")

/**
 * Whitespace token.
 */
object WhitespaceToken : NoInterpolationToken(" ")

/**
 * Assignment token.
 */
object AssignmentToken : NoInterpolationToken("=")

/**
 * Value token.
 */
class ValueToken(token: String) : NoInterpolationToken(token)