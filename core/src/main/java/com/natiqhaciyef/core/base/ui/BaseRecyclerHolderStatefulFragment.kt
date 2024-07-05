package com.natiqhaciyef.core.base.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.natiqhaciyef.core.store.AppStorePref
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.reflect.KClass


abstract class BaseRecyclerHolderStatefulFragment<VB : ViewBinding,
        VM : BaseViewModel<State, Event, Effect>,
        T: Any,
        RV : BaseRecyclerViewAdapter<T, *>,
        State : UiState,
        Event : UiEvent,
        Effect : UiEffect> :
    BaseFragment<VB, VM, State, Event, Effect>() {
    abstract var adapter: RV?
    abstract fun recyclerViewConfig(list: List<T>)

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}