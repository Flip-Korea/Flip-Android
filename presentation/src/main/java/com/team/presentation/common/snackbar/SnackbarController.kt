package com.team.presentation.common.snackbar

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

data class SnackbarEvent(
    val message: String,
    val action: SnackbarAction? = null
)

data class SnackbarAction(
    val name: String,
    val action: suspend () -> Unit
)

/**
 * Flip Snackbar Controller
 *
 * ### 사용 예시
 *     fun showSnackbar() {
 *         viewModelScope.launch {
 *             SnackbarController.sendEvent(
 *                 event = SnackbarEvent(
 *                     message = "Event Message",
 *                     action = SnackbarAction(
 *                         name = "Click me!",
 *                         action = {
 *                             SnackbarController.sendEvent(
 *                                 event = SnackbarEvent("Action Pressed!")
 *                             )
 *                         }
 *                     )
 *                 )
 *             )
 *         }
 *     }
 *
 *  @see SnackbarEvent
 *  @see SnackbarAction
 */
object SnackbarController {

    private val _events = Channel<SnackbarEvent>()
    val events = _events.receiveAsFlow()

    suspend fun sendEvent(event: SnackbarEvent) {
        _events.send(event)
    }
}