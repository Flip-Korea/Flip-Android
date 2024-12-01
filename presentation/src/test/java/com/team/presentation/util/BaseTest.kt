package com.team.presentation.util

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.team.presentation.TestDispatcherRule
import org.junit.Rule

open class BaseTest {
    @get:Rule
    val testDispatcher = TestDispatcherRule()

    /** 백그라운드 작업을 동기적으로 실행 */
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
}