package com.natiqhaciyef.prodocument.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel<State, Event, Effect> : ViewModel(), CoroutineScope {
    private val job = Job()

    private val currentState: State by lazy { getInitialState() }
    private var _state = MutableStateFlow(currentState)
    val state: StateFlow<State> = _state

    private var _event = MutableSharedFlow<Event>()
    private val event: SharedFlow<Event> = _event

    private val _effect = Channel<Effect>()
    val effect = _effect.receiveAsFlow()

    init {
        initEventSubscribers()
    }

    private fun initEventSubscribers() { event.onEach {
        onEventUpdate(it)
    }.launchIn(viewModelScope) }


    protected open fun onEventUpdate(event: Event){}


    abstract fun getInitialState(): State

    protected fun setBaseState(state: State) {
        viewModelScope.launch { _state.emit(state) }
    }

    fun getCurrentBaseState(): State {
        return currentState
    }

    fun postEvent(event: Event) {
        viewModelScope.launch { _event.emit(event) }
    }

    fun postEffect(effect: Effect){
        viewModelScope.launch { _effect.send(effect) }
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}