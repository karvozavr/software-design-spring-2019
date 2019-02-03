package ru.spb.hse.karvozavr.cli.parser

import ru.spb.hse.karvozavr.cli.shell.env.Environment

class ParseException(err: String) : RuntimeException(err)

object CommandParser {

    fun parse(command: String, env: Environment): List<CommandNode> =
        createPipeline(collapseTokens(interpolateTokens(tokenize(command), env)))

    private fun createPipeline(commandTokens: List<List<Token>>): List<CommandNode> =
        commandTokens.map { parseAssignment(it) ?: parseCommand(it) }

    private fun parseCommand(tokens: List<Token>): CommandNode =
        CommandNode(tokens.first().token, tokens.drop(1).map { it.token })


    private fun parseAssignment(tokens: List<Token>): CommandNode? {
        if (tokens.size == 3 && tokens[1] == AssignmentToken) {
            return CommandNode(
                "=",
                listOf(tokens.first().token, tokens.drop(2).map { it.token }.reduce { a, b -> a + b })
            )
        }
        return null
    }

    private fun collapseTokens(tokens: List<Token>): List<List<Token>> {
        val result = mutableListOf<List<Token>>()
        val value = StringBuilder()
        var currentResult = mutableListOf<Token>()

        for (token in tokens) {
            when (token) {
                is PipeToken -> {
                    if (value.isNotEmpty()) {
                        currentResult.add(ValueToken(value.toString()))
                        value.clear()
                    }
                    result.add(currentResult)
                    currentResult = mutableListOf()
                }
                is WhitespaceToken -> if (value.isNotEmpty()) {
                    currentResult.add(ValueToken(value.toString()))
                    value.clear()
                }
                is AssignmentToken -> {
                    if (value.isNotEmpty()) {
                        currentResult.add(ValueToken(value.toString()))
                        value.clear()
                    }
                    currentResult.add(AssignmentToken)
                }
                is ValueToken -> value.append(token.token)
            }
        }

        if (value.isNotEmpty()) {
            currentResult.add(ValueToken(value.toString()))
        }

        if (currentResult.isNotEmpty()) {
            result.add(currentResult)
        }

        return result.filter { it.isNotEmpty() }
    }

    private fun interpolateTokens(tokens: List<Token>, env: Environment): List<Token> {
        return tokens.map { it.interpolate(env) }
    }

    private fun tokenize(string: String): List<Token> {
        val tokens = mutableListOf<Token>()
        val stringBuilder = StringBuilder()

        var weakQuoting = false
        var strongQuoting = false

        for (c in string) {
            when (c) {
                ' ' -> if (weakQuoting || strongQuoting) {
                    stringBuilder.append(c)
                } else {
                    tokens.add(InterpolationToken(stringBuilder.toString()))
                    stringBuilder.clear()
                    tokens.add(WhitespaceToken)
                }
                '\'' -> if (strongQuoting) {
                    tokens.add(ValueToken(stringBuilder.toString()))
                    stringBuilder.clear()
                    strongQuoting = false
                } else if (weakQuoting) {
                    stringBuilder.append(c)
                } else {
                    strongQuoting = true
                }
                '\"' -> if (weakQuoting) {
                    tokens.add(InterpolationToken(stringBuilder.toString()))
                    stringBuilder.clear()
                    weakQuoting = false
                } else if (strongQuoting) {
                    stringBuilder.append(c)
                } else {
                    weakQuoting = true
                }
                '=' -> {
                    if (weakQuoting or strongQuoting) {
                        stringBuilder.append(c)
                    } else {
                        tokens.add(InterpolationToken(stringBuilder.toString()))
                        stringBuilder.clear()
                        tokens.add(AssignmentToken)
                    }
                }
                '|' -> if (weakQuoting or strongQuoting) {
                    stringBuilder.append(c)
                } else {
                    tokens.add(InterpolationToken(stringBuilder.toString()))
                    stringBuilder.clear()
                    tokens.add(PipeToken)
                }
                else -> stringBuilder.append(c)
            }
        }

        if (stringBuilder.isNotEmpty())
            tokens.add(InterpolationToken(stringBuilder.toString()))

        if (weakQuoting || strongQuoting) {
            throw ParseException("Mismatched quote.")
        }

        return tokens.filter { it.token.isNotEmpty() }
    }
}
