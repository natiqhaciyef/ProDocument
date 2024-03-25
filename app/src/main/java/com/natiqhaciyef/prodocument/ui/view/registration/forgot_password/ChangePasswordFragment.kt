package com.natiqhaciyef.prodocument.ui.view.registration.forgot_password

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.natiqhaciyef.common.helpers.toJsonString
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.databinding.AlertDialogResultViewBinding
import com.natiqhaciyef.prodocument.databinding.FragmentChangePasswordBinding
import com.natiqhaciyef.prodocument.ui.base.BaseFragment
import com.natiqhaciyef.prodocument.ui.store.AppStorePrefKeys
import com.natiqhaciyef.prodocument.ui.store.AppStorePrefKeys.TOKEN_KEY
import com.natiqhaciyef.prodocument.ui.util.InputAcceptanceConditions.checkPasswordAcceptanceCondition
import com.natiqhaciyef.prodocument.ui.view.registration.forgot_password.viewmodel.ChangePasswordViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ChangePasswordFragment : BaseFragment<FragmentChangePasswordBinding, ChangePasswordViewModel>(
    FragmentChangePasswordBinding::inflate,
    ChangePasswordViewModel::class
) {
//    private val viewModel: ChangePasswordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val navData: ChangePasswordFragmentArgs by navArgs()
//        val email = navData.email

        binding.apply {
            continueButton.setOnClickListener { onClickAction("") }
            rememberMeCheckBox.onClickAction()

            newPasswordText.changeVisibility()
            newPasswordConfirmText.changeVisibility()
            newPasswordConfirmText.setPasswordTitleText(requireContext().getString(com.natiqhaciyef.common.R.string.confirm_password))
            newPasswordConfirmText.setPasswordHintText(requireContext().getString(com.natiqhaciyef.common.R.string.confirm_password))
        }
        passwordValidation()
        confirmPasswordValidation()
    }

    private fun onClickAction(email: String) {
        viewModel?.apply {
//            updatePassword(email, binding.newPasswordText.text.toString())
            updateResultState.observe(viewLifecycleOwner) { tokenState ->
                lifecycleScope.launch {
                    if (tokenState.isSuccess && tokenState.obj != null) {
                        dataStore.saveParcelableClassData(
                            context = requireContext(),
                            data = tokenState?.obj!!,
                            key = TOKEN_KEY
                        )

                        createResultAlertDialog()
                    }
                }
            }
        }
    }

    private fun createResultAlertDialog() {
        val binding = AlertDialogResultViewBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(true)
            setContentView(binding.root)

            binding.resultButton.setOnClickListener {
                navigate(R.id.loginFragment)
                dismiss()
            }
//            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.setFlags(
                WindowManager.LayoutParams.FLAG_DIM_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND
            )
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            show()
        }
    }


    private fun passwordValidation() {
        binding.apply {
            newPasswordText.customDoOnTextChangeListener { text, i, i2, i3 ->
                continueButton.isEnabled = checkPasswordAcceptanceCondition(text)
            }
        }
    }

    private fun confirmPasswordValidation() {
        binding.apply {
            newPasswordConfirmText.customDoOnTextChangeListener { text, i, i2, i3 ->
                continueButton.isEnabled = checkPasswordAcceptanceCondition(text)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}