package com.team.presentation.common.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

interface BaseUiState

interface BaseUiEvent

interface BaseUiEffect

/**
 * MVI 패턴을 사용하는 Base ViewModel
 */
abstract class FlipBaseViewModel<State: BaseUiState, Event: BaseUiEvent, Effect: BaseUiEffect>: ViewModel() {

    /** [createInitialState]를 통해 초기 상태를 지정한다. */
    private val initialState : State by lazy { createInitialState() }
    abstract fun createInitialState() : State

    /** UI State */
    private val _uiState: MutableStateFlow<State> = MutableStateFlow(initialState)
    val uiState: StateFlow<State> = _uiState.asStateFlow()
    val currentUiState: State
        get() = uiState.value

    /** UI Effect, 일회성 이벤트를 위한 상태 값 (Channel 이용) */
    private val _effect = Channel<Effect>()
    val effect = _effect.receiveAsFlow()

    /** Update UiState */
    protected fun updateState(newUiState: State) {
        _uiState.update { newUiState }
    }
    protected fun updateState(reducer: State.() -> State) {
        val newState = currentUiState.reducer()
        _uiState.update { newState }
    }

    /** Handle UiEvent */
    fun processEvent(event: Event) {
        viewModelScope.launch {
            handleEvent(event)
        }
    }
    protected abstract suspend fun handleEvent(event: Event)

    /** Send UiEffect */
    protected fun CoroutineScope.sendEffect(effect: Effect) {
        this.launch { _effect.send(effect) }
    }

    override fun onCleared() {
        super.onCleared()
        _effect.close()
    }
}