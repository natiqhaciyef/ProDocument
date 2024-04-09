package com.natiqhaciyef.prodocument.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
abstract class BaseViewModel
//<State, Event>
@Inject constructor() : ViewModel(), CoroutineScope {
    private val job = Job()

//    private val currentState: BaseUIState<State> by lazy { getInitialState() }
//    private var _state = MutableStateFlow(currentState)
//    private val state: StateFlow<BaseUIState<State>> = _state
//
//    private var _event = MutableSharedFlow<Event>()
//    val event: SharedFlow<Event> = _event
//
//    init {
//        initEventSubscribers()
//    }
//    private fun initEventSubscribers() { event.onEach {  }.launchIn(viewModelScope) }
//
//    protected open fun onEventUpdate(event: Event){}
//
//
//    abstract fun getInitialState(): BaseUIState<State>
//
//    protected fun setBaseState(state: BaseUIState<State>) {
//        viewModelScope.launch { _state.emit(state) }
//    }
//
//    fun getCurrentBaseState(): BaseUIState<State> {
//        return currentState
//    }
//
//    fun postEvent(event: Event) {
//        viewModelScope.launch { _event.emit(event) }
//    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}