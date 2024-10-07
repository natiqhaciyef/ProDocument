package com.natiqhaciyef.core.base.ui


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel<State : UiState, Event : UiEvent, Effect : UiEffect> : ViewModel(),
    CoroutineScope {
    private val job = Job()

    private val currentState: State by lazy { getInitialState() }
    private var _state = MutableStateFlow(currentState)
    val state: StateFlow<State> = _state
    val stateValue = _state.value
    private var holdState: State = _state.value

    private var _event = MutableSharedFlow<Event>()
    private val event: SharedFlow<Event> = _event

    private val _effect = Channel<Effect>()
    val effect = _effect.receiveAsFlow()
    private var eventJob: Job? = null

    init {
        initEventSubscribers()
    }

    private fun initEventSubscribers() {
        eventJob = event.onEach {
            onEventUpdate(it)
        }.launchIn(viewModelScope)
    }


    protected open fun onEventUpdate(event: Event) {}


    abstract fun getInitialState(): State

    protected fun setBaseState(state: State) {
        viewModelScope.launch { _state.emit(state) }
    }

    fun getHoldState(): State {
        return holdState
    }

    fun holdBaseState(state: State) {
        holdState = state
    }

    fun getCurrentBaseState(): State {
        return currentState
    }

    fun postEvent(event: Event) {
        viewModelScope.launch { _event.emit(event) }
    }

    suspend fun updateEvent(event: Event) {
        viewModelScope.launch { _event.emit(event) }
    }

    fun postEffect(effect: Effect) {
        viewModelScope.launch { _effect.send(effect) }
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCleared() {
        super.onCleared()
        job.cancel()
        eventJob?.cancel()
    }
}