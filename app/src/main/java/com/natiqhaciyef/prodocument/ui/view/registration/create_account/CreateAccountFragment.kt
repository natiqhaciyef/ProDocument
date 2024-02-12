package com.natiqhaciyef.prodocument.ui.view.registration.create_account

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.databinding.FragmentCreateAccountBinding
import com.natiqhaciyef.prodocument.ui.base.BaseFragment
import com.natiqhaciyef.prodocument.ui.util.emailRegex
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateAccountFragment : BaseFragment() {
    private lateinit var binding: FragmentCreateAccountBinding
    private val viewModel: CompleteProfileViewModel by viewModels()
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
        with(binding) {
            passwordVisibility()
            confirmPasswordVisibility()
            emailValidation()
            passwordValidation()

            finishButton.setOnClickListener { finishButtonClickAction() }
        }
    }

    private fun finishButtonClickAction() {
        binding.apply {
            viewModel.state.observe(viewLifecycleOwner) {
                val email = createAccountEmailInput.text.toString()
                val password = createAccountPasswordInput.text.toString()
                viewModel.collectDataFromCreateAccountScreen(
                    data = it.copy(email = email, password = password),
                    onSuccess = {
                        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                    },
                    onFail = { exception ->
                        Toast.makeText(
                            requireContext(),
                            "Error: ${exception?.localizedMessage}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
        }
    }

    private fun passwordVisibility() {
        binding.apply {
            passwordVisibilityIcon.setOnClickListener {
                isPasswordVisible = !isPasswordVisible
                if (isPasswordVisible) {
                    binding.createAccountPasswordInput.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    passwordVisibilityIcon.setImageResource(R.drawable.visibility_on_icon)
                } else {
                    binding.createAccountPasswordInput.transformationMethod =
                        PasswordTransformationMethod.getInstance()
                    passwordVisibilityIcon.setImageResource(R.drawable.visibility_off_icon)
                }
            }
        }
    }

    private fun confirmPasswordVisibility() {
        binding.apply {
            confirmPasswordVisibilityIcon.setOnClickListener {
                isConfirmPasswordVisible = !isConfirmPasswordVisible
                if (isConfirmPasswordVisible) {
                    binding.createAccountConfirmPasswordInput.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    confirmPasswordVisibilityIcon.setImageResource(R.drawable.visibility_on_icon)
                } else {
                    binding.createAccountConfirmPasswordInput.transformationMethod =
                        PasswordTransformationMethod.getInstance()
                    confirmPasswordVisibilityIcon.setImageResource(R.drawable.visibility_off_icon)
                }
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
            createAccountPasswordInput.doOnTextChanged { text, _, _, _ ->
                finishButton.isEnabled = checkPasswordAcceptanceCondition(text)
                        && checkEmailAcceptanceCondition(createAccountEmailInput.text)
            }
        }

        binding.apply {
            createAccountPasswordInput.doOnTextChanged { text, _, _, _ ->
                finishButton.isEnabled = binding.createAccountPasswordInput.text == text
                        && checkEmailAcceptanceCondition(createAccountEmailInput.text)
                        && checkPasswordAcceptanceCondition(text)
            }
        }
    }

    private fun checkEmailAcceptanceCondition(text: CharSequence?) =
        !text.isNullOrEmpty() //&& text.matches(emailRegex)

    private fun checkPasswordAcceptanceCondition(text: CharSequence?) =
        !text.isNullOrEmpty() && text.length >= PASSWORD_MIN_LENGTH

    companion object {
        private const val PASSWORD_MIN_LENGTH = 8
    }
}