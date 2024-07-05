package com.natiqhaciyef.prodocument.ui.view.registration.create_account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.natiqhaciyef.common.model.mapped.MappedTokenModel
import com.natiqhaciyef.common.model.mapped.MappedUserModel
import com.natiqhaciyef.core.store.AppStorePrefKeys.TOKEN_KEY
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.databinding.FragmentCreateAccountBinding
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.ui.util.InputAcceptanceConditions.checkEmailAcceptanceCondition
import com.natiqhaciyef.prodocument.ui.util.InputAcceptanceConditions.checkPasswordAcceptanceCondition
import com.natiqhaciyef.prodocument.ui.view.registration.RegistrationActivity
import com.natiqhaciyef.prodocument.ui.view.registration.create_account.contract.CreateAccountContract
import com.natiqhaciyef.prodocument.ui.view.registration.create_account.viewmodel.CreateAccountViewModel
import com.natiqhaciyef.uikit.alert.AlertDialogManager.createResultAlertDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

@AndroidEntryPoint
class CreateAccountFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCreateAccountBinding = FragmentCreateAccountBinding::inflate,
    override val viewModelClass: KClass<CreateAccountViewModel> = CreateAccountViewModel::class
) : BaseFragment<FragmentCreateAccountBinding, CreateAccountViewModel, CreateAccountContract.CreateAccountState, CreateAccountContract.CreateAccountEvent, CreateAccountContract.CreateAccountEffect>() {
    private var isRemembered: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data: CreateAccountFragmentArgs by navArgs()
        config()
        with(binding) {
            emailValidation()
            passwordValidation()

            goBackIcon.setOnClickListener { navigateBack() }
            finishButton.setOnClickListener { finishButtonClickAction(data.user) }
        }
    }

    override fun onStateChange(state: CreateAccountContract.CreateAccountState) {
        when {
            state.isLoading -> {
                changeVisibilityOfProgressBar(true)
            }

            else -> {
                changeVisibilityOfProgressBar()

                if (state.token != null) {
                    tokenStoring(state.token)
                }
            }
        }
    }

    override fun onEffectUpdate(effect: CreateAccountContract.CreateAccountEffect) {
        when (effect) {
            is CreateAccountContract.CreateAccountEffect.FieldNotCorrectlyFilledEffect -> {
                Toast.makeText(requireContext(), effect.message, Toast.LENGTH_SHORT)
                    .show()
            }

            is CreateAccountContract.CreateAccountEffect.UserCreationFailedEffect -> {
                Toast.makeText(requireContext(), effect.message, Toast.LENGTH_SHORT)
                    .show()
            }

            is CreateAccountContract.CreateAccountEffect.UserCreationSucceedEffect -> {
                createResultAlertDialog()
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

    private fun config() {
        binding.apply {
            createAccountConfirmPasswordInput.setPasswordTitleText(com.natiqhaciyef.common.R.string.confirm_password)
            createAccountConfirmPasswordInput.setPasswordHintText(com.natiqhaciyef.common.R.string.confirm_password)

            createAccountPasswordInput.changeVisibility()
            createAccountConfirmPasswordInput.changeVisibility()

            rememberMeCheckBoxImage.onClickAction()
        }
    }

    private fun finishButtonClickAction(userModel: MappedUserModel?) {
        userModel?.let {
            binding.apply {
                val email = createAccountEmailInput.getInputResult()
                val password = createAccountPasswordInput.getPasswordText()
                userModel.email = email
                userModel.password = password

                finishButtonClickEvent(userModel)
            }
        }
    }

    private fun finishButtonClickEvent(userModel: MappedUserModel) {
        viewModel.postEvent(
            CreateAccountContract
                .CreateAccountEvent.FinishButtonClickEvent(user = userModel)
        )
    }

    private fun tokenStoring(tokenModel: MappedTokenModel?) {
        lifecycleScope.launch {
            if (tokenModel != null) {
                dataStore.saveParcelableClassData(
                    context = requireContext(),
                    data = tokenModel,
                    key = TOKEN_KEY
                )
            }
        }
    }

    private fun createResultAlertDialog() {
        (requireActivity() as RegistrationActivity).createResultAlertDialog{ resultDialog ->
            resultDialog.dismiss()
            navigate(R.id.loginFragment)
        }
    }

    private fun emailValidation() {
        binding.apply {
            createAccountEmailInput.listenUserInput { text, start, before, count ->
                finishButton.isEnabled = checkEmailAcceptanceCondition(text)
                        && checkPasswordAcceptanceCondition(createAccountPasswordInput.getPasswordText())
            }
        }
    }

    private fun passwordValidation() {
        binding.apply {
            createAccountPasswordInput.customDoOnTextChangeListener { text, _, _, _ ->
                finishButton.isEnabled = checkPasswordAcceptanceCondition(text)
                        && checkEmailAcceptanceCondition(createAccountEmailInput.getInputResult())
            }
        }

        binding.apply {
            createAccountConfirmPasswordInput.customDoOnTextChangeListener { text, _, _, _ ->
                finishButton.isEnabled = checkPasswordAcceptanceCondition(text)
                        && checkEmailAcceptanceCondition(createAccountEmailInput.getInputResult())
            }
        }
    }
}