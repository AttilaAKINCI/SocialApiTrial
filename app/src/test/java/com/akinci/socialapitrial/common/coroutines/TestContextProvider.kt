package com.akinci.socialapitrial.common.coroutines

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
//TODO boyle de calisir muhtemelen, daha temiz olur
class TestContextProvider(val testCoroutineDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()) : CoroutineContextProvider() {
    override val Main: CoroutineContext = testCoroutineDispatcher
    override val IO: CoroutineContext = testCoroutineDispatcher
}
