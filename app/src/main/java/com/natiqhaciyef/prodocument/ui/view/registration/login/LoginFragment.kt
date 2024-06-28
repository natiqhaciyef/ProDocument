package com.natiqhaciyef.prodocument.ui.view.registration.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.natiqhaciyef.common.constants.EMPTY_FIELD
import com.natiqhaciyef.common.constants.SOMETHING_WENT_WRONG
import com.natiqhaciyef.common.model.mapped.MappedTokenModel
import com.natiqhaciyef.core.store.AppStorePrefKeys.TOKEN_KEY
import com.natiqhaciyef.prodocument.databinding.FragmentLoginBinding
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.ui.manager.NavigationManager.HOME_ROUTE
import com.natiqhaciyef.prodocument.ui.manager.NavigationManager.navigateByActivityTitle
import com.natiqhaciyef.prodocument.ui.util.InputAcceptanceConditions.checkEmailAcceptanceCondition
import com.natiqhaciyef.prodocument.ui.util.InputAcceptanceConditions.checkPasswordAcceptanceCondition
import com.natiqhaciyef.prodocument.ui.view.registration.login.contract.LoginContract
import com.natiqhaciyef.prodocument.ui.view.registration.login.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

@AndroidEntryPoint
class LoginFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLoginBinding = FragmentLoginBinding::inflate,
    override val viewModelClass: KClass<LoginViewModel> = LoginViewModel::class
) : BaseFragment<FragmentLoginBinding, LoginViewModel, LoginContract.LoginState, LoginContract.LoginEvent, LoginContract.LoginEffect>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        config()
        emailValidation()
        passwordValidation()
    }

    override fun onStateChange(state: LoginContract.LoginState) {
        when{
            state.isLoading -> {
                changeVisibilityOfProgressBar(true)
            }

            else -> {
                changeVisibilityOfProgressBar()
                if (state.tokenModel != null){
                    loginButtonClickAction(state.tokenModel!!)
                }
            }
        }
    }

    override fun onEffectUpdate(effect: LoginContract.LoginEffect) {
        when(effect){
            is LoginContract.LoginEffect.LoginFailedEffect -> {
                Toast.makeText(
                    requireContext(),
                    effect.exception?.localizedMessage ?: SOMETHING_WENT_WRONG,
                    Toast.LENGTH_SHORT
                ).show()
            }

            is LoginContract.LoginEffect.EmptyFieldEffect -> {
                Toast.makeText(
                    requireContext(),
                    EMPTY_FIELD,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
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

            signInButton.setOnClickListener { loginButtonClickEvent() }
            forgotPasswordText.setOnClickListener {
                val action = LoginFragmentDirections.actionLoginFragmentToForgotPasswordNavGraph()
                navigate(action)
            }
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

    private fun loginButtonClickEvent() {
        binding.apply {
            val email = loginEmailInput.getInputResult()
            val password = loginPasswordInput.text

            viewModel.postEvent(LoginContract
                .LoginEvent.LoginClickEvent(ctx = requireContext(), email = email, password = password))
        }
    }

    private fun loginButtonClickAction(token: MappedTokenModel){
        lifecycleScope.launch {
            dataStore.saveParcelableClassData(
                context = requireContext(),
                data = token,
                key = TOKEN_KEY
            )
            navigateByActivityTitle(HOME_ROUTE, requireActivity(),true)
        }
    }


    private fun emailValidation() {
        binding.apply {
            loginEmailInput.listenUserInput { text, start, before, count ->
                signInButton.isEnabled = checkEmailAcceptanceCondition(text)
                        && checkPasswordAcceptanceCondition(loginPasswordInput.getPasswordText())
            }
        }
    }

    private fun passwordValidation() {
        binding.apply {
            loginPasswordInput.customDoOnTextChangeListener { text, _, _, _ ->
                signInButton.isEnabled = checkPasswordAcceptanceCondition(text)
                        && checkEmailAcceptanceCondition(loginEmailInput.getInputResult())
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}