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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OTPFragment : BaseFragment() {
    private lateinit var binding: FragmentOTPBinding
    private val otpViewModel: OTPViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOTPBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data: OTPFragmentArgs by navArgs()
        val email = data.emailData

        binding.apply {
            goBackIcon.setOnClickListener { navigateBack() }
            customOTPView.buttonEnablingListener(confirmButton)
            confirmButton.setOnClickListener { onClickAction(email) }

            resendTimingText.text = Html.fromHtml(
                requireContext().getString(R.string.resend_description, "60"),
                Html.FROM_HTML_MODE_COMPACT
            )
        }
        textHtmlConfig()
    }


    private fun onClickAction(email: String) {
        otpViewModel.apply {
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
                otpViewModel.timingFlow.collectLatest {
                    resendTimingText.text = Html.fromHtml(
                        requireContext().getString(R.string.resend_description, "$it"),
                        Html.FROM_HTML_MODE_COMPACT
                    )
                }
            }
        }
    }
}