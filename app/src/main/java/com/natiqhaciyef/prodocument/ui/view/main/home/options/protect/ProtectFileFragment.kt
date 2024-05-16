package com.natiqhaciyef.prodocument.ui.view.main.home.options.protect

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import coil.load
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.databinding.FragmentProtectFileBinding
import com.natiqhaciyef.prodocument.ui.manager.FileManager
import com.natiqhaciyef.prodocument.ui.util.BaseNavigationDeepLink
import com.natiqhaciyef.prodocument.ui.util.BundleConstants
import com.natiqhaciyef.prodocument.ui.view.main.home.options.protect.contract.ProtectFileContract
import com.natiqhaciyef.prodocument.ui.view.main.home.options.protect.viewmodel.ProtectFileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

@AndroidEntryPoint
class ProtectFileFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProtectFileBinding = FragmentProtectFileBinding::inflate,
    override val viewModelClass: KClass<ProtectFileViewModel> = ProtectFileViewModel::class
) : BaseFragment<FragmentProtectFileBinding, ProtectFileViewModel, ProtectFileContract.ProtectFileState, ProtectFileContract.ProtectFileEvent, ProtectFileContract.ProtectFileEffect>() {
    private var bundle = bundleOf()
    private val fileRequestLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let { intent ->
                    if (intent.data != null)
                        FileManager.readAndCreateFile(
                            activity = requireActivity(),
                            uri = intent.data!!
                        ) { file ->
                            val title = binding.usernameWatermarkTitle.text.toString()

                            fileConfig(file)
                            binding.continueButton.isEnabled = true
                            binding.continueButton.setOnClickListener { continueButtonAction(file, title) }
                        }
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        config()
    }

    override fun onStateChange(state: ProtectFileContract.ProtectFileState) {
        when {
            state.isLoading -> {
                changeVisibilityOfProgressBar(true)
            }

            else -> {
                changeVisibilityOfProgressBar()

            }
        }
    }

    override fun onEffectUpdate(effect: ProtectFileContract.ProtectFileEffect) {

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
        with(binding) {
            addFileButton.setOnClickListener { FileManager.getFile(fileRequestLauncher) }
            goBackIcon.setOnClickListener { goBackIconClickAction() }
        }
    }

    private fun fileConfig(file: MappedMaterialModel) {
        with(binding) {
            filePreviewImage.load(file.image)
            fileTitleText.text = file.title
            fileDateText.text = file.createdDate

            fileRemoveIcon.setOnClickListener { fileRemoveClickAction() }
            filePreviewObject.visibility = View.VISIBLE
            addFileButton.visibility = View.GONE
        }
    }

    private fun continueButtonAction(materialModel: MappedMaterialModel, title: String) {
        bundle.putParcelable(BundleConstants.BUNDLE_MATERIAL, materialModel)
        bundle.putString(BundleConstants.BUNDLE_TYPE, PROTECT_TYPE)
        bundle.putString(BundleConstants.BUNDLE_TITLE, title)
        val action =
            ProtectFileFragmentDirections.actionProtectFileFragmentToAddPasswordFileFragment(bundle)
        navigate(action)
    }

    private fun fileRemoveClickAction() {
        binding.apply {
            filePreviewObject.visibility = View.GONE
            continueButton.isEnabled = false
            addFileButton.visibility = View.VISIBLE
        }
    }

    private fun goBackIconClickAction() {
        BaseNavigationDeepLink.navigateByRouteTitle(
            this@ProtectFileFragment,
            BaseNavigationDeepLink.HOME_ROUTE
        )
    }

    companion object {
        const val PROTECT_TYPE = "ProtectPdf"
    }
}