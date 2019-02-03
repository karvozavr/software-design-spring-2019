package ru.spb.hse.karvozavr.cli.pipeline

import ru.spb.hse.karvozavr.cli.commands.Command
import ru.spb.hse.karvozavr.cli.parser.CommandNode
import ru.spb.hse.karvozavr.cli.commands.CommandFactory
import ru.spb.hse.karvozavr.cli.shell.CliShell
import ru.spb.hse.karvozavr.cli.shell.Shell
import ru.spb.hse.karvozavr.cli.streams.InStream
import ru.spb.hse.karvozavr.cli.streams.OutStream
import ru.spb.hse.karvozavr.cli.streams.ReadWriteStream

/**
 * Tools to create pipeline from parsed commands
 * with given IO and environment configuration.
 */
object PipelineFactory {

    /**
     * Creates executable pipeline.
     *
     * @param commands parsed commands
     * @param inStream input stream
     * @param outStream output stream
     * @param errStream error stream
     *
     * @return executable pipeline
     */
    fun createPipeline(
        commands: List<CommandNode>,
        inStream: InStream,
        outStream: OutStream,
        errStream: OutStream,
        shell: Shell
    ): Pipeline = when (commands.size) {
        0 -> EmptyPipeline
        1 -> SingleCommandPipeline(
            CommandFactory.createCommand(
                commands.first().name,
                commands.first().args,
                inStream,
                outStream,
                errStream,
                shell,
                false
            )
        )
        else -> createMultiCommandPipeline(
            commands,
            inStream,
            outStream,
            errStream
        )
    }

    private fun createMultiCommandPipeline(
        commands: List<CommandNode>,
        inStream: InStream,
        outStream: OutStream,
        errStream: OutStream
    ): MultiCommandPipeline {
        val pipeline = mutableListOf<Command>()
        var bufferStream = ReadWriteStream()
        var cmd = CommandFactory.createCommand(
            commands.first().name,
            commands.first().args,
            inStream,
            bufferStream,
            errStream,
            CliShell.emptyShell(),
            false
        )
        pipeline.add(cmd)

        for (i in 1 until (commands.size - 1)) {
            val outBufferStream = ReadWriteStream()
            cmd = CommandFactory.createCommand(
                commands[i].name,
                commands[i].args,
                bufferStream,
                outBufferStream,
                errStream,
                CliShell.emptyShell(),
                true
            )
            pipeline.add(cmd)
            bufferStream = outBufferStream
        }

        cmd = CommandFactory.createCommand(
            commands.last().name,
            commands.last().args,
            bufferStream,
            outStream,
            errStream,
            CliShell.emptyShell(),
            true
        )
        pipeline.add(cmd)

        return MultiCommandPipeline(pipeline)
    }
}
