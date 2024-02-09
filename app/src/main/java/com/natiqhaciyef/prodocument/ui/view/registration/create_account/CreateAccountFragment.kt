package com.natiqhaciyef.prodocument.ui.view.registration.create_account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.databinding.FragmentCreateAccountBinding
import com.natiqhaciyef.prodocument.ui.base.BaseFragment
import com.natiqhaciyef.prodocument.ui.util.emailRegex
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateAccountFragment : BaseFragment() {
    private lateinit var binding: FragmentCreateAccountBinding
    private var isPasswordVisible = false
    private var isConfirmPasswordVisible = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        passwordVisibility()
        confirmPasswordVisibility()
        emailValidation()
        passwordValidation()
    }

    private fun passwordVisibility() {
        binding.apply {
            passwordVisibilityIcon.setOnClickListener {
                isPasswordVisible = !isPasswordVisible
                if (isPasswordVisible)
                    passwordVisibilityIcon.setImageResource(R.drawable.visibility_on_icon)
                else
                    passwordVisibilityIcon.setImageResource(R.drawable.visibility_off_icon)
            }
        }
    }

    private fun confirmPasswordVisibility() {
        binding.apply {
            confirmPasswordVisibilityIcon.setOnClickListener {
                isConfirmPasswordVisible = !isConfirmPasswordVisible
                if (isConfirmPasswordVisible)
                    confirmPasswordVisibilityIcon.setImageResource(R.drawable.visibility_on_icon)
                else
                    confirmPasswordVisibilityIcon.setImageResource(R.drawable.visibility_off_icon)
            }
        }
    }

    private fun emailValidation() {
        binding.apply {
            createAccountEmailInput.doOnTextChanged { text, start, before, count ->
                finishButton.isEnabled = checkEmailAcceptanceCondition(text)
                        && checkPasswordAcceptanceCondition(createAccountPasswordInput.text)
            }
        }
    }

    private fun passwordValidation() {
        binding.apply {
            createAccountPasswordInput.doOnTextChanged { text, start, before, count ->
                finishButton.isEnabled = checkPasswordAcceptanceCondition(text)
                        && checkEmailAcceptanceCondition(createAccountEmailInput.text)
            }
        }
    }

    private fun checkEmailAcceptanceCondition(text: CharSequence?) =
        !text.isNullOrEmpty() && text.matches(emailRegex)

    private fun checkPasswordAcceptanceCondition(text: CharSequence?) =
        !text.isNullOrEmpty() && text.length >= PASSWORD_MIN_LENGTH && text == binding.createAccountConfirmPasswordInput.text.toString()

    companion object {
        private const val PASSWORD_MIN_LENGTH = 8
    }
}