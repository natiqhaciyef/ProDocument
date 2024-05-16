package com.natiqhaciyef.prodocument.ui.view.main.home.options.compress

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.databinding.FragmentCompressBinding
import com.natiqhaciyef.prodocument.ui.view.main.home.options.compress.contract.CompressContract
import com.natiqhaciyef.prodocument.ui.view.main.home.options.compress.viewmodel.CompressViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlin.reflect.KClass

@AndroidEntryPoint
class CompressFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCompressBinding = FragmentCompressBinding::inflate,
    override val viewModelClass: KClass<CompressViewModel> = CompressViewModel::class
) : BaseFragment<FragmentCompressBinding, CompressViewModel, CompressContract.CompressState, CompressContract.CompressEvent, CompressContract.CompressEffect>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStateChange(state: CompressContract.CompressState) {
        when{
            state.isLoading -> {

            }

            else -> {}
        }
    }

    override fun onEffectUpdate(effect: CompressContract.CompressEffect) {

    }
}