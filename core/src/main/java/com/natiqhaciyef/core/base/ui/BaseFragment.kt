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
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.natiqhaciyef.common.model.mapped.MappedTokenModel
import com.natiqhaciyef.common.constants.MATERIAL_TOKEN_MOCK_KEY
import com.natiqhaciyef.core.store.AppStorePref
import com.natiqhaciyef.core.store.AppStorePrefKeys
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel<State, Event, Effect>, State : UiState, Event : UiEvent, Effect : UiEffect> :
    Fragment() {
    abstract val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB
    abstract val viewModelClass: KClass<VM>

    protected var _binding: VB? = null
    val binding: VB
        get() = _binding!!

    val viewModel: VM
        get() {
            viewModelClass.let { return ViewModelProvider(this)[viewModelClass.java] }
        }

    val dataStore = AppStorePref

    protected open fun onStateChange(state: State) {}

    protected open fun onEffectUpdate(effect: Effect) {}

    override fun onDestroy() {
        super.onDestroy()
//        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindInflater(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onStateSubscribers()
    }

    private fun onStateSubscribers() {
        viewModel.state.onEach {
            onStateChange(it)
        }.launchIn(viewModel.viewModelScope)

        viewModel.effect.onEach {
            onEffectUpdate(it)
        }.launchIn(viewModel.viewModelScope)
    }



    fun navigateBack() {
        findNavController().popBackStack()
    }

    fun navigate(id: Int) {
        findNavController().navigate(id)
    }

    fun navigate(direction: NavDirections) {
        findNavController().navigate(direction)
    }

    fun navigateByDeepLink(deepLink: Uri) {
        requireActivity().startActivity(Intent(Intent.ACTION_VIEW, deepLink))
    }

    fun navigate(
        activity: FragmentActivity,
        intent: Intent,
        isFinished: Boolean = false
    ) {
        activity.startActivity(intent)
        if (isFinished)
            activity.finish()
    }

    fun navigate(deepLink: Uri) {
        val pendingIntent = NavDeepLinkBuilder(requireContext())
            .setDestination(deepLink.toString())
            .createPendingIntent()

        pendingIntent.send()
    }

    protected fun getToken(onSuccess: (String) -> Unit = { }) = lifecycleScope.launch {
        val result = dataStore.readParcelableClassData(
            context = requireContext(),
            key = AppStorePrefKeys.TOKEN_KEY,
            classType = MappedTokenModel::class.java
        )

        onSuccess(result?.accessToken ?: MATERIAL_TOKEN_MOCK_KEY)
        return@launch
    }
}