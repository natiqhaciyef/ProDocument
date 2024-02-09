package com.natiqhaciyef.prodocument.ui.view.registration.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.databinding.FragmentLetsSignInBinding
import com.natiqhaciyef.prodocument.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LetsSignInFragment : BaseFragment() {
    private lateinit var binding: FragmentLetsSignInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLetsSignInBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
//            googleSignInButton.initBinding()
            googleSignInButton.setIconImage(R.drawable.google)
            googleSignInButton.setTitleText("Continue with Google")

            facebookSignInButton.setIconImage(R.drawable.facebook)
            facebookSignInButton.setTitleText("Continue with Facebook")

            appleSignInButton.setIconImage(R.drawable.apple)
            appleSignInButton.setTitleText("Continue with Apple")

            goToSignInButton.setOnClickListener {  }
            goToSignUpButtonText.setOnClickListener{ navigate(R.id.completeProfileFragment) }
        }
    }
}