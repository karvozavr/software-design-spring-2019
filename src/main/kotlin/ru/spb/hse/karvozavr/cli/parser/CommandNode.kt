package ru.spb.hse.karvozavr.cli.parser

/**
 * Parsed command AST node.
 */
data class CommandNode(val name: String, val args: List<String>)