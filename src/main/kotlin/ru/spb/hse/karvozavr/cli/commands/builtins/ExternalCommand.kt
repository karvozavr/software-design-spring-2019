package ru.spb.hse.karvozavr.cli.commands.builtins

import ru.spb.hse.karvozavr.cli.commands.Command
import ru.spb.hse.karvozavr.cli.shell.Shell
import ru.spb.hse.karvozavr.cli.streams.InStream
import ru.spb.hse.karvozavr.cli.streams.OutStream
import ru.spb.hse.karvozavr.cli.util.ExitCode
import java.io.File
import java.nio.file.Files

/**
 * External command.
 * Invokes external process on execution.
 */
class ExternalCommand(
    args: List<String>,
    inputStream: InStream,
    outStream: OutStream,
    errStream: OutStream,
    shell: Shell,
    val command: String,
    val redirect: Boolean = false
) : Command(args, inputStream, outStream, errStream, shell) {

    private val inFile = Files.createTempFile("pipetempin", ".tmp")
    private val outFile = Files.createTempFile("pipetempout", ".tmp")

    init {
        inFile.toFile().deleteOnExit()
        outFile.toFile().deleteOnExit()
    }

    override fun execute(): ExitCode {
        return try {
            val commandList = listOf(command) + args
            var pb = ProcessBuilder()
                .command(commandList)
            pb = if (redirect)
                pb.redirectInput(inputStreamToFile())
            else
                pb.redirectInput(ProcessBuilder.Redirect.INHERIT)

            val exitCode = pb.redirectOutput(outFile.toFile())
                .start()
                .waitFor()

            outputFileToStream()

            if (exitCode == 0) {
                ExitCode.SUCCESS
            } else {
                ExitCode.UNKNOWN_CODE
            }
        } catch (e: Exception) {
            writeError("Unknown command: $command")
            ExitCode.UNKNOWN_COMMAND
        }
    }

    private fun inputStreamToFile(): File {
        Files.newBufferedWriter(inFile).use {
            while (inputStream.isNotEmpty()) {
                it.write(inputStream.scanLine())
            }
        }
        return inFile.toFile()
    }

    private fun outputFileToStream() {
        Files.lines(outFile).forEach { writeLine(it) }
    }
}