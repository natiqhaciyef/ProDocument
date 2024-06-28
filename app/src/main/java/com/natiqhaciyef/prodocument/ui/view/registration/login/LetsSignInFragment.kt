package com.natiqhaciyef.prodocument.ui.view.registration.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.natiqhaciyef.common.helpers.capitalizeFirstLetter
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.databinding.FragmentLetsSignInBinding
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.ui.view.registration.login.contract.LoginContract
import com.natiqhaciyef.prodocument.ui.view.registration.login.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass


@AndroidEntryPoint
class LetsSignInFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLetsSignInBinding = FragmentLetsSignInBinding::inflate,
    override val viewModelClass: KClass<LoginViewModel> = LoginViewModel::class
) : BaseFragment<FragmentLetsSignInBinding, LoginViewModel, LoginContract.LoginState, LoginContract.LoginEvent, LoginContract.LoginEffect>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
//            googleSignInButton.initBinding()
            googleSignInButton.setIconImage(com.natiqhaciyef.common.R.drawable.google)
            googleSignInButton.setTitleText(
                getString(
                    com.natiqhaciyef.common.R.string.continue_with_platform_name,
                    GOOGLE.lowercase().capitalizeFirstLetter()
                )
            )
            googleSignInButton.setOnClickListener { }

            facebookSignInButton.setIconImage(com.natiqhaciyef.common.R.drawable.facebook)
            facebookSignInButton.setTitleText(
                getString(
                    com.natiqhaciyef.common.R.string.continue_with_platform_name,
                    FACEBOOK.lowercase().capitalizeFirstLetter()
                )
            )
            facebookSignInButton.setOnClickListener { }

            appleSignInButton.setIconImage(com.natiqhaciyef.common.R.drawable.apple)
            appleSignInButton.setTitleText(
                getString(
                    com.natiqhaciyef.common.R.string.continue_with_platform_name,
                    APPLE.lowercase().capitalizeFirstLetter()
                )
            )
            appleSignInButton.setOnClickListener { }

            goToSignInButton.setOnClickListener { navigate(R.id.loginFragment) }
            goToSignUpButtonText.setOnClickListener {
                navigate(LetsSignInFragmentDirections.actionLetsSignInFragmentToCompleteProfileFragment())
            }
        }
    }

    private fun socialMediaButtonClick(type: String) = when (type) {
        GOOGLE -> {
            googleSignInAction()
        }

        APPLE -> {
            appleSignInAction()
        }

        FACEBOOK -> {
            facebookSignInAction()
        }

        else -> {}
    }


    private fun googleSignInAction() {

    }

    private fun appleSignInAction() {

    }

    private fun facebookSignInAction() {

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val GOOGLE = "GOOGLE"
        private const val APPLE = "APPLE"
        private const val FACEBOOK = "FACEBOOK"
    }
}