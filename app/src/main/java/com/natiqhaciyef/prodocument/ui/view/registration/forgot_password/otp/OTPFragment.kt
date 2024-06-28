package com.natiqhaciyef.prodocument.ui.view.registration.forgot_password.otp

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.natiqhaciyef.common.constants.EMPTY_STRING
import com.natiqhaciyef.prodocument.databinding.FragmentOTPBinding
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.ui.view.registration.forgot_password.otp.contract.OTPContract
import com.natiqhaciyef.prodocument.ui.view.registration.forgot_password.otp.viewmodel.OTPViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

@AndroidEntryPoint
class OTPFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOTPBinding = FragmentOTPBinding::inflate,
    override val viewModelClass: KClass<OTPViewModel> = OTPViewModel::class
) : BaseFragment<FragmentOTPBinding, OTPViewModel, OTPContract.OTPState, OTPContract.OTPEvent, OTPContract.OTPEffect>() {
    private var email: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data: OTPFragmentArgs by navArgs()
        email = data.emailData

        binding.apply {
            goBackIcon.setOnClickListener { navigateBack() }
            customOTPView.buttonEnablingListener(confirmButton)
            confirmButton.setOnClickListener { onClickEvent(customOTPView.getOTP()) }

            resendTimingText.text = Html.fromHtml(
                requireContext().getString(
                    com.natiqhaciyef.common.R.string.resend_description,
                    "60"
                ),
                Html.FROM_HTML_MODE_COMPACT
            )
        }
        textHtmlConfig()
    }

    override fun onStateChange(state: OTPContract.OTPState) {
        when {
            state.isLoading -> {
                changeVisibilityOfProgressBar(true)
            }

            else -> {
                changeVisibilityOfProgressBar()
                if (state.result != null) {
                    onClickAction(email ?: EMPTY_STRING)
                }
            }
        }
    }

    override fun onEffectUpdate(effect: OTPContract.OTPEffect) {
        when (effect) {
            is OTPContract.OTPEffect.FailEffect -> {}
        }
    }

    private fun changeVisibilityOfProgressBar(isVisible: Boolean = false) {
        if (isVisible) {
            binding.apply {
                uiLayout.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
                progressBar.isIndeterminate = true
            }
        } else {
            binding.apply {
                uiLayout.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                progressBar.isIndeterminate = false
            }
        }
    }

    private fun onClickAction(email: String) {
        val action = OTPFragmentDirections.actionOTPFragmentToChangePasswordFragment(email)
        navigate(action)
    }

    private fun onClickEvent(otp: String) {
        viewModel.postEvent(OTPContract.OTPEvent.SendOTP(otp))
    }

    private fun textHtmlConfig() {
        binding.apply {
            lifecycleScope.launch {
                viewModel.timingFlow.collectLatest {
                    resendTimingText.text = Html.fromHtml(
                        requireContext().getString(
                            com.natiqhaciyef.common.R.string.resend_description,
                            "$it"
                        ),
                        Html.FROM_HTML_MODE_COMPACT
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}