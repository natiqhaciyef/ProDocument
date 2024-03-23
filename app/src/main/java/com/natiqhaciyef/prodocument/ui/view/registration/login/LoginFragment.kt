package com.natiqhaciyef.prodocument.ui.view.registration.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.common.objects.ErrorMessages
import com.natiqhaciyef.prodocument.databinding.FragmentLoginBinding
import com.natiqhaciyef.prodocument.ui.base.BaseFragment
import com.natiqhaciyef.prodocument.ui.base.BaseNavigationDeepLink.HOME_ROUTE
import com.natiqhaciyef.prodocument.ui.store.AppStorePrefKeys
import com.natiqhaciyef.prodocument.ui.store.AppStorePrefKeys.MATERIAL_TOKEN_KEY
import com.natiqhaciyef.prodocument.ui.store.AppStorePrefKeys.PREMIUM_TOKEN_KEY
import com.natiqhaciyef.prodocument.ui.store.AppStorePrefKeys.TOKEN_KEY
import com.natiqhaciyef.prodocument.ui.util.InputAcceptanceConditions.checkEmailAcceptanceCondition
import com.natiqhaciyef.prodocument.ui.util.InputAcceptanceConditions.checkPasswordAcceptanceCondition
import com.natiqhaciyef.prodocument.ui.view.registration.login.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        config()
        emailValidation()
        passwordValidation()
    }

    private fun config() {
        with(binding) {
            loginPasswordInput.changeVisibility()

            googleSignInButton.changeImageHorizontalBias(0.5f)
            googleSignInButton.changeTextVisibility(View.GONE)

            appleSignInButton.changeImageHorizontalBias(0.5f)
            appleSignInButton.changeTextVisibility(View.GONE)
            appleSignInButton.setIconImage(com.natiqhaciyef.common.R.drawable.apple)

            facebookSignInButton.changeImageHorizontalBias(0.5f)
            facebookSignInButton.changeTextVisibility(View.GONE)
            facebookSignInButton.setIconImage(com.natiqhaciyef.common.R.drawable.facebook)

            rememberMeCheckBoxImage.onClickAction()

            signInButton.setOnClickListener { loginButtonClickAction() }
            forgotPasswordText.setOnClickListener {
                val action = LoginFragmentDirections.actionLoginFragmentToForgotPasswordNavGraph()
                navigate(action)
            }
        }
    }

    private fun loginButtonClickAction() {
        binding.apply {
            val email = loginEmailInput.text.toString()
            val password = loginPasswordInput.text.toString()

            loginViewModel.signIn(
                email = email,
                password = password,
                onSuccess = {
                    observeTokenState()
                },
                onFail = { exception ->
                    Toast.makeText(
                        requireContext(),
                        exception?.localizedMessage ?: ErrorMessages.SOMETHING_WENT_WRONG,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    }

    private fun observeTokenState() {
        loginViewModel.tokenState.observe(viewLifecycleOwner) { tokenState ->
            lifecycleScope.launch {
                if (tokenState.isSuccess && tokenState.obj != null) {
                    dataStore.saveString(
                        context = requireContext(),
                        data = tokenState.obj!!.uid.toString(),
                        key = TOKEN_KEY
                    )

                    dataStore.saveString(
                        context = requireContext(),
                        data = tokenState.obj!!.materialToken.toString(),
                        key = MATERIAL_TOKEN_KEY
                    )

                    if (tokenState.obj!!.premiumToken != null) {
                        dataStore.saveString(
                            context = requireContext(),
                            data = tokenState.obj!!.premiumToken.toString(),
                            key = PREMIUM_TOKEN_KEY
                        )
                    }

                    navigateByActivityTitle(HOME_ROUTE, true)
                }
            }
        }
    }


    private fun emailValidation() {
        binding.apply {
            loginEmailInput.doOnTextChanged { text, start, before, count ->
                signInButton.isEnabled = checkEmailAcceptanceCondition(text)
                        && checkPasswordAcceptanceCondition(loginPasswordInput.getPasswordText())
            }
        }
    }

    private fun passwordValidation() {
        binding.apply {
            loginPasswordInput.customDoOnTextChangeListener { text, _, _, _ ->
                signInButton.isEnabled = checkPasswordAcceptanceCondition(text)
                        && checkEmailAcceptanceCondition(loginEmailInput.text)
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}