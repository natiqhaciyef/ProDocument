package com.natiqhaciyef.prodocument.ui.view.registration.forgot_password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import com.natiqhaciyef.prodocument.databinding.FragmentForgotPasswordBinding
import com.natiqhaciyef.prodocument.ui.base.BaseFragment
import com.natiqhaciyef.prodocument.ui.util.InputAcceptanceConditions.checkEmailAcceptanceCondition
import com.natiqhaciyef.prodocument.ui.view.registration.forgot_password.viewmodel.ForgotPasswordViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ForgotPasswordFragment : BaseFragment<FragmentForgotPasswordBinding>() {
    private val forgotPasswordViewModel: ForgotPasswordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            goBackIcon.setOnClickListener { navigateBack() }
            continueButton.setOnClickListener { onClickAction(forgotPasswordEmailInput.text.toString()) }
        }
        emailValidation()
    }

    private fun emailValidation() {
        binding.apply {
            forgotPasswordEmailInput.doOnTextChanged { text, start, before, count ->
                continueButton.isEnabled = checkEmailAcceptanceCondition(text)
            }
        }
    }

    private fun onClickAction(email: String) {
//        forgotPasswordViewModel.apply {
//        getOtpResult(binding.forgotPasswordEmailInput.text.toString())
//        otpResultState.observe(viewLifecycleOwner) {
//            if (it.obj != null && it.isSuccess) {
        val action =
            ForgotPasswordFragmentDirections.actionForgotPasswordFragment2ToOTPFragment2(email)
        navigate(action)
//            }
//        }
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}