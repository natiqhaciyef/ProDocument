package com.natiqhaciyef.prodocument.ui.view.registration.forgot_password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.databinding.FragmentForgotPasswordBinding
import com.natiqhaciyef.prodocument.ui.base.BaseFragment
import com.natiqhaciyef.prodocument.ui.util.InputAcceptanceConditions.checkEmailAcceptanceCondition
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ForgotPasswordFragment : BaseFragment() {
    private lateinit var binding: FragmentForgotPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        config()
        emailValidation()
    }

    private fun config() {
        binding.apply {
            goBackIcon.setOnClickListener { navigateBack() }
            continueButton.setOnClickListener { onClickAction() }
        }
    }

    private fun emailValidation() {
        binding.apply {
            forgotPasswordEmailInput.doOnTextChanged { text, start, before, count ->
                continueButton.isEnabled = checkEmailAcceptanceCondition(text)
            }
        }
    }

    private fun onClickAction() {
        navigate(R.id.OTPFragment)
    }
}