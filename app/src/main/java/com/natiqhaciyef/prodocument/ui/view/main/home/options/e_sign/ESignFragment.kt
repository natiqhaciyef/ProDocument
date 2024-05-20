package com.natiqhaciyef.prodocument.ui.view.main.home.options.e_sign

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.databinding.FragmentESignBinding
import com.natiqhaciyef.prodocument.ui.view.main.home.options.e_sign.contract.ESignContract
import com.natiqhaciyef.prodocument.ui.view.main.home.options.e_sign.viewmodel.ESignViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass


@AndroidEntryPoint
class ESignFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentESignBinding = FragmentESignBinding::inflate,
    override val viewModelClass: KClass<ESignViewModel> = ESignViewModel::class
) : BaseFragment<FragmentESignBinding, ESignViewModel, ESignContract.ESignState, ESignContract.ESignEvent, ESignContract.ESignEffect>() {


}