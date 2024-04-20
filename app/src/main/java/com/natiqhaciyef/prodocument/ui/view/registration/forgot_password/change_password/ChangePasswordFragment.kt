package com.natiqhaciyef.prodocument.ui.view.registration.forgot_password.change_password

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.natiqhaciyef.common.model.mapped.MappedTokenModel
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.databinding.AlertDialogResultViewBinding
import com.natiqhaciyef.prodocument.databinding.FragmentChangePasswordBinding
import com.natiqhaciyef.prodocument.ui.base.BaseFragment
import com.natiqhaciyef.prodocument.ui.store.AppStorePrefKeys.TOKEN_KEY
import com.natiqhaciyef.prodocument.ui.util.InputAcceptanceConditions.checkPasswordAcceptanceCondition
import com.natiqhaciyef.prodocument.ui.view.registration.forgot_password.change_password.contract.ChangePasswordContract
import com.natiqhaciyef.prodocument.ui.view.registration.forgot_password.change_password.viewmodel.ChangePasswordViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.reflect.KClass


@AndroidEntryPoint
class ChangePasswordFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentChangePasswordBinding = FragmentChangePasswordBinding::inflate,
    override val viewModelClass: KClass<ChangePasswordViewModel> = ChangePasswordViewModel::class
) : BaseFragment<FragmentChangePasswordBinding, ChangePasswordViewModel, ChangePasswordContract.ChangePasswordState, ChangePasswordContract.ChangePasswordEvent, ChangePasswordContract.ChangePasswordEffect>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navData: ChangePasswordFragmentArgs by navArgs()
        val email = navData.email

        binding.apply {
            continueButton.setOnClickListener { onClickEvent(email) }
            rememberMeCheckBox.onClickAction()

            newPasswordText.changeVisibility()
            newPasswordConfirmText.changeVisibility()
            newPasswordConfirmText.setPasswordTitleText(requireContext().getString(com.natiqhaciyef.common.R.string.confirm_password))
            newPasswordConfirmText.setPasswordHintText(requireContext().getString(com.natiqhaciyef.common.R.string.confirm_password))
        }
        passwordValidation()
        confirmPasswordValidation()
    }

    override fun onStateChange(state: ChangePasswordContract.ChangePasswordState) {
        when {
            state.isLoading -> {
                // add progress bar
                changeVisibilityOfProgressBar(true)
            }

            else -> {
                changeVisibilityOfProgressBar()
                if (state.tokenModel != null){ tokenStoring(tokenState = state.tokenModel!!) }
            }
        }
    }

    override fun onEffectUpdate(effect: ChangePasswordContract.ChangePasswordEffect) {
        when (effect) {
            is ChangePasswordContract.ChangePasswordEffect.ResultAlertDialog -> {
                createResultAlertDialog(effect.icon, effect.messageType, effect.messageDescription)
            }
        }
    }

    private fun onClickEvent(email: String) {
        viewModel.postEvent(
            ChangePasswordContract.ChangePasswordEvent.UpdatePasswordEvent(
                email = email, password = binding.newPasswordText.text.toString()
            )
        )
    }

    private fun tokenStoring(tokenState: MappedTokenModel) {
        lifecycleScope.launch {
            dataStore.saveParcelableClassData(
                context = requireContext(),
                data = tokenState,
                key = TOKEN_KEY
            )
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

    private fun createResultAlertDialog(resultIcon: Int, successMsg: String, description: String) {
        val binding = AlertDialogResultViewBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(true)
            setContentView(binding.root)

            binding.resultIcon.setImageResource(com.natiqhaciyef.common.R.drawable.result_dialog_container_icon)
            binding.resultTypeImage.setImageResource(resultIcon)
            binding.resultTitle.text = getString(
                com.natiqhaciyef.common.R.string.change_password_alert_dialog_title,
                successMsg.lowercase()
            )

            binding.resultDescription.text = description

            binding.resultButton.setOnClickListener {
                navigate(R.id.loginFragment)
                dismiss()
            }

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