package ru.spb.hse.karvozavr.cli.parser

import ru.spb.hse.karvozavr.cli.shell.env.Environment
import java.lang.StringBuilder

sealed class Token(val token: String) {
    abstract fun interpolate(env: Environment): Token
}

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

sealed class NoInterpolationToken(token: String) : Token(token) {
    override fun interpolate(env: Environment): NoInterpolationToken {
        return this
    }
}

object PipeToken : NoInterpolationToken("|")

object WhitespaceToken : NoInterpolationToken(" ")

object AssignmentToken : NoInterpolationToken("=")

class ValueToken(token: String) : NoInterpolationToken(token)