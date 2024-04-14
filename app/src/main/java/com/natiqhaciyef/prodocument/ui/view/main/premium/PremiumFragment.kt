package com.natiqhaciyef.prodocument.ui.view.main.premium

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.natiqhaciyef.prodocument.databinding.FragmentPremiumBinding
import com.natiqhaciyef.prodocument.ui.base.BaseFragment
import com.natiqhaciyef.prodocument.ui.view.main.premium.event.PremiumEvent
import com.natiqhaciyef.prodocument.ui.view.main.premium.viewmodel.PremiumViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

@AndroidEntryPoint
class PremiumFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPremiumBinding = FragmentPremiumBinding::inflate,
    override val viewModelClass: KClass<PremiumViewModel> = PremiumViewModel::class
) : BaseFragment<FragmentPremiumBinding, PremiumViewModel, PremiumEvent>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}