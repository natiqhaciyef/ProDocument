package com.natiqhaciyef.prodocument.ui.view.registration.forgot_password.forgot_password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import com.natiqhaciyef.prodocument.databinding.FragmentForgotPasswordBinding
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.ui.util.InputAcceptanceConditions.checkEmailAcceptanceCondition
import com.natiqhaciyef.prodocument.ui.view.registration.forgot_password.forgot_password.contract.ForgotPasswordContract
import com.natiqhaciyef.prodocument.ui.view.registration.forgot_password.forgot_password.viewmoodel.ForgotPasswordViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass


@AndroidEntryPoint
class ForgotPasswordFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentForgotPasswordBinding = FragmentForgotPasswordBinding::inflate,
    override val viewModelClass: KClass<ForgotPasswordViewModel> = ForgotPasswordViewModel::class
) : BaseFragment<FragmentForgotPasswordBinding, ForgotPasswordViewModel,
        ForgotPasswordContract.ForgotPasswordState,
        ForgotPasswordContract.ForgotPasswordEvent,
        ForgotPasswordContract.ForgotPasswordEffect>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            goBackIcon.setOnClickListener { navigateBack() }
            continueButton.setOnClickListener { onClickEvent(forgotPasswordEmailInput.getInputResult()) }
        }
        emailValidation()
    }

    override fun onStateChange(state: ForgotPasswordContract.ForgotPasswordState) {
        when {
            state.isLoading -> {
                changeVisibilityOfProgressBar(true)
            }

            else -> {
                changeVisibilityOfProgressBar()
                if (state.result != null){
                    onClickAction(binding.forgotPasswordEmailInput.getInputResult())
                }
            }
        }
    }

    override fun onEffectUpdate(effect: ForgotPasswordContract.ForgotPasswordEffect) {
        when (effect) {
            is ForgotPasswordContract.ForgotPasswordEffect.FailEffect -> {}
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


    private fun emailValidation() {
        binding.apply {
            forgotPasswordEmailInput.listenUserInput{ text, start, before, count ->
                continueButton.isEnabled = checkEmailAcceptanceCondition(text)
            }
        }
    }

    private fun onClickEvent(email: String) {
        viewModel.postEvent(ForgotPasswordContract.ForgotPasswordEvent.GetOtpEvent(
            email = email
        ))
    }

    private fun onClickAction(email: String) {
        val action =
            ForgotPasswordFragmentDirections.actionForgotPasswordFragment2ToOTPFragment2(email)
        navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}