package com.natiqhaciyef.prodocument.ui.view.registration.create_account

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.databinding.AlertDialogResultViewBinding
import com.natiqhaciyef.prodocument.databinding.FragmentCreateAccountBinding
import com.natiqhaciyef.prodocument.ui.base.BaseFragment
import com.natiqhaciyef.prodocument.ui.store.AppStorePrefKeys
import com.natiqhaciyef.prodocument.ui.store.AppStorePrefKeys.MATERIAL_TOKEN_KEY
import com.natiqhaciyef.prodocument.ui.store.AppStorePrefKeys.TOKEN_KEY
import com.natiqhaciyef.prodocument.ui.util.InputAcceptanceConditions.checkEmailAcceptanceCondition
import com.natiqhaciyef.prodocument.ui.util.InputAcceptanceConditions.checkPasswordAcceptanceCondition
import com.natiqhaciyef.prodocument.ui.view.registration.create_account.viewmodel.CompleteProfileViewModel
import com.natiqhaciyef.prodocument.ui.view.registration.create_account.viewmodel.CreateAccountViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateAccountFragment : BaseFragment<FragmentCreateAccountBinding>() {
    private val createAccountViewModel: CreateAccountViewModel by viewModels()
    private val completeProfileViewModel: CompleteProfileViewModel by viewModels()
    private var isRemembered: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        config()
        with(binding) {
            emailValidation()
            passwordValidation()

            goBackIcon.setOnClickListener { navigateBack() }
            finishButton.setOnClickListener { finishButtonClickAction() }
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

    private fun finishButtonClickAction() {
        binding.apply {
            val email = createAccountEmailInput.text.toString()
            val password = createAccountPasswordInput.getPasswordText().toString()

            completeProfileViewModel.userState.observe(viewLifecycleOwner) { baseUiState ->
                baseUiState?.email = email
                baseUiState?.password = password

                createAccountViewModel.clickButtonAction(
                    mappedUserModel = baseUiState,
                    onSuccess = {
                        if (isRemembered) {
                            createAccountViewModel.saveToDatabase(baseUiState) { tokenObserving() }
                        } else {
                            tokenObserving()
                        }
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

    private fun tokenObserving() {
        createAccountViewModel.tokenState.observe(viewLifecycleOwner) { tokenState ->
            lifecycleScope.launch {
                if (tokenState.obj != null && tokenState.isSuccess) {
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
                            key = AppStorePrefKeys.PREMIUM_TOKEN_KEY
                        )
                    }

                    createResultAlertDialog()
                }
            }
        }
    }

    private fun createResultAlertDialog() {
        val binding = AlertDialogResultViewBinding.inflate(layoutInflater)
        val resultDialog =
            AlertDialog.Builder(requireContext(), com.natiqhaciyef.common.R.style.CustomAlertDialog)
                .setView(binding.root)
                .setCancelable(true)
                .create()

        binding.resultButton.setOnClickListener {
            resultDialog.dismiss()
            navigate(R.id.loginFragment)
        }

        resultDialog.show()
    }

    private fun emailValidation() {
        binding.apply {
            createAccountEmailInput.doOnTextChanged { text, start, before, count ->
                finishButton.isEnabled = checkEmailAcceptanceCondition(text)
                        && checkPasswordAcceptanceCondition(createAccountPasswordInput.getPasswordText())
            }
        }
    }

    private fun passwordValidation() {
        binding.apply {
            createAccountPasswordInput.customDoOnTextChangeListener { text, _, _, _ ->
                finishButton.isEnabled = checkPasswordAcceptanceCondition(text)
                        && checkEmailAcceptanceCondition(createAccountEmailInput.text)
            }
        }

        binding.apply {
            createAccountConfirmPasswordInput.customDoOnTextChangeListener { text, _, _, _ ->
                finishButton.isEnabled = checkPasswordAcceptanceCondition(text)
                        && checkEmailAcceptanceCondition(createAccountEmailInput.text)
            }
        }
    }
}