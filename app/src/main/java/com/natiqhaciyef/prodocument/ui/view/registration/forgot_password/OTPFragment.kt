package com.natiqhaciyef.prodocument.ui.view.registration.forgot_password

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.databinding.FragmentOTPBinding
import com.natiqhaciyef.prodocument.ui.base.BaseFragment
import com.natiqhaciyef.prodocument.ui.view.registration.forgot_password.event.OTPEvent
import com.natiqhaciyef.prodocument.ui.view.registration.forgot_password.viewmodel.OTPViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

@AndroidEntryPoint
class OTPFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOTPBinding = FragmentOTPBinding::inflate,
    override val viewModelClass: KClass<OTPViewModel> = OTPViewModel::class
) : BaseFragment<FragmentOTPBinding, OTPViewModel, OTPEvent>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data: OTPFragmentArgs by navArgs()
        val email = data.emailData

        binding.apply {
            goBackIcon.setOnClickListener { navigateBack() }
            customOTPView.buttonEnablingListener(confirmButton)
            confirmButton.setOnClickListener { onClickAction(email) }

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


    private fun onClickAction(email: String) {
        viewModel?.apply {
//            sendOtp(otp = binding.customOTPView.getOTP())
//            otpResultState.observe(viewLifecycleOwner) { state ->
//            if (state.isSuccess && state.obj != null) {
            val action = OTPFragmentDirections.actionOTPFragmentToChangePasswordFragment(email)
            navigate(action)
//            }
//            }
        }
    }

    private fun textHtmlConfig() {
        binding.apply {
            lifecycleScope.launch {
                viewModel?.timingFlow?.collectLatest {
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