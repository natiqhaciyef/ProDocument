package com.natiqhaciyef.prodocument.ui.view.main.home.options.e_sign

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.navArgs
import com.natiqhaciyef.common.constants.SOMETHING_WENT_WRONG
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.databinding.FragmentESignBinding
import com.natiqhaciyef.prodocument.ui.util.BUNDLE_SIGN_BITMAP
import com.natiqhaciyef.prodocument.ui.view.main.home.options.e_sign.contract.ESignContract
import com.natiqhaciyef.prodocument.ui.view.main.home.options.e_sign.viewmodel.ESignViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass


@AndroidEntryPoint
class ESignFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentESignBinding = FragmentESignBinding::inflate,
    override val viewModelClass: KClass<ESignViewModel> = ESignViewModel::class
) : BaseFragment<FragmentESignBinding, ESignViewModel, ESignContract.ESignState, ESignContract.ESignEvent, ESignContract.ESignEffect>() {
    private var resBundle = bundleOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: ESignFragmentArgs by navArgs()
        resBundle = args.resourceBundle

        binding.continueButton.setOnClickListener {
            getBitmapFromViewEvent()
        }
    }

    override fun onStateChange(state: ESignContract.ESignState) {
        when {
            state.isLoading -> {
                binding.uiLayout.loadingState(true)
            }

            else -> {
                binding.uiLayout.successState()

                if (state.signBitmap != null)
                    continueButtonClickAction(state.signBitmap!!)
            }
        }
    }

    override fun onEffectUpdate(effect: ESignContract.ESignEffect) {

    }

    private fun continueButtonClickAction(signBitmap: Bitmap) {
        resBundle.putParcelable(BUNDLE_SIGN_BITMAP, signBitmap)
        val action = ESignFragmentDirections.actionESignFragmentToAddSignFragment(resBundle)
        navigate(action)
    }

    private fun getBitmapFromViewEvent(){
        viewModel.postEvent(ESignContract.ESignEvent.ConvertSignToBitmap(view = binding.customCanvasDrawView))
    }
}