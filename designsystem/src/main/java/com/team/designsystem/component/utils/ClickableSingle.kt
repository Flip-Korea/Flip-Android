package com.team.designsystem.component.utils

internal interface ClickableSingle {
    fun onEvent(event: () -> Unit)

    companion object
}

internal fun ClickableSingle.Companion.get(): ClickableSingle =
    ClickableSingleImpl()

private class ClickableSingleImpl : ClickableSingle {
    private val now: Long
        get() = System.currentTimeMillis()

    private var lastEventTimeMs: Long = 0

    override fun onEvent(event: () -> Unit) {
        if (now - lastEventTimeMs >= 300L) {
            event.invoke()
        }
        lastEventTimeMs = now
    }
}