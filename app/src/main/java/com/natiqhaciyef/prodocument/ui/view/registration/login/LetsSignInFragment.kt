package com.natiqhaciyef.prodocument.ui.view.registration.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.databinding.FragmentLetsSignInBinding
import com.natiqhaciyef.prodocument.ui.base.BaseFragment
import com.natiqhaciyef.prodocument.ui.base.BaseViewModel
import com.natiqhaciyef.prodocument.ui.view.registration.login.event.LoginEvent
import com.natiqhaciyef.prodocument.ui.view.registration.login.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass


@AndroidEntryPoint
class LetsSignInFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLetsSignInBinding = FragmentLetsSignInBinding::inflate,
    override val viewModelClass: KClass<LoginViewModel> = LoginViewModel::class
) : BaseFragment<FragmentLetsSignInBinding, LoginViewModel, LoginEvent>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
//            googleSignInButton.initBinding()
            googleSignInButton.setIconImage(com.natiqhaciyef.common.R.drawable.google)
            googleSignInButton.setTitleText("Continue with Google")
            googleSignInButton.setOnClickListener { }

            facebookSignInButton.setIconImage(com.natiqhaciyef.common.R.drawable.facebook)
            facebookSignInButton.setTitleText("Continue with Facebook")
            facebookSignInButton.setOnClickListener { }

            appleSignInButton.setIconImage(com.natiqhaciyef.common.R.drawable.apple)
            appleSignInButton.setTitleText("Continue with Apple")
            appleSignInButton.setOnClickListener { }

            goToSignInButton.setOnClickListener { navigate(R.id.loginFragment) }
            goToSignUpButtonText.setOnClickListener { navigate(R.id.completeProfileFragment) }
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

    companion object {
        private const val GOOGLE = "GOOGLE"
        private const val APPLE = "APPLE"
        private const val FACEBOOK = "FACEBOOK"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}