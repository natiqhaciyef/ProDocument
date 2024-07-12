package com.natiqhaciyef.prodocument.ui.view.main.home.options.protect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.navArgs
import com.natiqhaciyef.common.constants.EIGHT
import com.natiqhaciyef.common.constants.SOMETHING_WENT_WRONG
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.databinding.FragmentAddPasswordFileBinding
import com.natiqhaciyef.prodocument.ui.util.BUNDLE_MATERIAL
import com.natiqhaciyef.prodocument.ui.util.BUNDLE_TYPE
import com.natiqhaciyef.prodocument.ui.util.NavigationUtil.PROTECT_TYPE
import com.natiqhaciyef.prodocument.ui.view.main.home.options.protect.contract.ProtectFileContract
import com.natiqhaciyef.prodocument.ui.view.main.home.options.protect.viewmodel.ProtectFileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

@AndroidEntryPoint
class AddPasswordFileFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAddPasswordFileBinding = FragmentAddPasswordFileBinding::inflate,
    override val viewModelClass: KClass<ProtectFileViewModel> = ProtectFileViewModel::class
) : BaseFragment<FragmentAddPasswordFileBinding, ProtectFileViewModel, ProtectFileContract.ProtectFileState, ProtectFileContract.ProtectFileEvent, ProtectFileContract.ProtectFileEffect>() {
    private var resourceBundle = bundleOf()
    private var material: MappedMaterialModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textConfiguration()
        val argument: AddPasswordFileFragmentArgs by navArgs()
        resourceBundle = argument.resourceBundle
        material = resourceBundle.getParcelable(BUNDLE_MATERIAL)
        config()
        backButtonConfig()
    }

    override fun onStateChange(state: ProtectFileContract.ProtectFileState) {
        when{
            state.isLoading -> {
                changeVisibilityOfProgressBar(true)
                errorResultConfig()
            }

            isIdleState(state) -> {
                changeVisibilityOfProgressBar()
                errorResultConfig(true)
            }

            else -> {
                changeVisibilityOfProgressBar()
                errorResultConfig(false)

                if (state.material != null)
                    protectFileAction(state.material!!)
            }
        }
    }

    override fun onEffectUpdate(effect: ProtectFileContract.ProtectFileEffect) {

    }

    private fun config() {
        buttonEnableCheck()

        with(binding) {
            filePassword.changeVisibility()
            filePasswordConfirm.changeVisibility()
            protectButton.setOnClickListener {
                if (checkPasswordConfirmation()) {
                    if (material != null) {
                        viewModel.postEvent(ProtectFileContract.ProtectFileEvent.ProtectFileWithKeyEvent(
                            key = binding.filePassword.getPasswordText(),
                            material = material!!
                        ))
                    }
                }
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

    private fun errorResultConfig(isVisible: Boolean = false){
        with(binding){
            notFoundLayout.visibility = if (isVisible) View.VISIBLE else View.GONE
            uiLayout.visibility = if (isVisible) View.GONE else View.VISIBLE

            notFoundDescription.text = getString(com.natiqhaciyef.common.R.string.files_loading_error_description_result)
            notFoundTitle.text = SOMETHING_WENT_WRONG
        }
    }

    private fun protectFileAction(material: MappedMaterialModel){
        resourceBundle.putParcelable(BUNDLE_MATERIAL, material)
        resourceBundle.putString(BUNDLE_TYPE, PROTECT_TYPE)

        val action = AddPasswordFileFragmentDirections
            .actionAddPasswordFileFragmentToPreviewMaterialNavGraph(resourceBundle)
        navigate(action)
    }

    private fun textConfiguration() {
        with(binding) {
            filePasswordConfirm.setPasswordTitleText(requireContext().getString(com.natiqhaciyef.common.R.string.confirm_password))
            filePasswordConfirm.setPasswordHintText(requireContext().getString(com.natiqhaciyef.common.R.string.confirm_password))
        }
    }

    private fun buttonEnableCheck() {
        with(binding) {
            filePassword.customDoOnTextChangeListener { charSequence, i, i2, i3 ->
                protectButton.isEnabled =
                    charSequence?.toString() == filePasswordConfirm.getPasswordText()
            }

            filePasswordConfirm.customDoOnTextChangeListener { charSequence, i, i2, i3 ->
                protectButton.isEnabled = charSequence?.toString() == filePassword.getPasswordText()
            }
        }
    }

    private fun backButtonConfig(){
        binding.goBackIcon.setOnClickListener { navigateBack() }
    }

    private fun checkPasswordConfirmation(): Boolean {
        return binding.filePassword.getPasswordText() == binding.filePasswordConfirm.getPasswordText() && binding.filePassword.getPasswordText().length >= EIGHT
    }
}