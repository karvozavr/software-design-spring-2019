package ru.spb.hse.karvozavr.cli.pipeline

import org.junit.Test

import org.junit.Assert.*
import ru.spb.hse.karvozavr.cli.util.ExitCode

class EmptyPipelineTest {

    @Test
    fun testSuccessExitCode() {
        val pipeline: Pipeline = EmptyPipeline
        assertEquals(ExitCode.SUCCESS, pipeline.execute())
    }
}