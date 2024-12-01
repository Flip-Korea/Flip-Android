package com.team.presentation.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class ViewModelTestTemplate {
    private val coroutineScope = CoroutineScope(UnconfinedTestDispatcher())
    private var job: Job? = null

    fun launch(block: suspend () -> Unit) = runTest(coroutineScope.coroutineContext) {
        job = coroutineScope.launch {
            block()
        }
    }

    fun `when`(event: () -> Unit) = runTest(coroutineScope.coroutineContext) {
        event()
        advanceTimeBy(1.seconds)
    }

    fun then(assertion: () -> Unit) = runTest(coroutineScope.coroutineContext) {
        assertion()
        job?.cancel()
    }
}
