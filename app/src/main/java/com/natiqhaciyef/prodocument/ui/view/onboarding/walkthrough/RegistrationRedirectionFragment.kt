package com.natiqhaciyef.prodocument.ui.view.onboarding.walkthrough

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.databinding.FragmentRegistrationRedirectionBinding
import com.natiqhaciyef.prodocument.ui.util.NavigationUtil
import com.natiqhaciyef.prodocument.ui.util.NavigationUtil.HOME_ROUTE
import com.natiqhaciyef.prodocument.ui.view.main.MainActivity
import com.natiqhaciyef.uikit.manager.FingerPrintManager
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegistrationRedirectionFragment : Fragment() {
    private var _binding: FragmentRegistrationRedirectionBinding? = null
    private val binding: FragmentRegistrationRedirectionBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegistrationRedirectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (FingerPrintManager.isBiometricReady(requireContext()))
            FingerPrintManager.showBiometricPrompt(
                title = requireActivity().getString(com.natiqhaciyef.common.R.string.biometric_id),
                subtitle = requireActivity().getString(com.natiqhaciyef.common.R.string.biometric_subtitle),
                description = requireActivity().getString(com.natiqhaciyef.common.R.string.biometric_details),
                activity = requireActivity() as AppCompatActivity
            ) { isSucceed, exception ->

                if (isSucceed){
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    requireActivity().startActivity(intent)
                    requireActivity().finish()
                }
            }
    }
}