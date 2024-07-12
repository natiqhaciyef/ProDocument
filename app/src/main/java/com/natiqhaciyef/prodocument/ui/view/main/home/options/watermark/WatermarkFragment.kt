package com.natiqhaciyef.prodocument.ui.view.main.home.options.watermark

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import coil.load
import com.natiqhaciyef.common.constants.SOMETHING_WENT_WRONG
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.prodocument.databinding.FragmentWatermarkBinding
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.uikit.manager.FileManager
import com.natiqhaciyef.prodocument.ui.util.BUNDLE_MATERIAL
import com.natiqhaciyef.prodocument.ui.util.BUNDLE_TITLE
import com.natiqhaciyef.prodocument.ui.util.BUNDLE_TYPE
import com.natiqhaciyef.prodocument.ui.util.NavigationUtil.HOME_ROUTE
import com.natiqhaciyef.prodocument.ui.util.NavigationUtil.WATERMARK_TYPE
import com.natiqhaciyef.prodocument.ui.util.NavigationUtil.navigateByRouteTitle
import com.natiqhaciyef.prodocument.ui.view.main.home.options.watermark.contract.WatermarkContract
import com.natiqhaciyef.prodocument.ui.view.main.home.options.watermark.viewmodel.WatermarkViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass


@AndroidEntryPoint
class WatermarkFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentWatermarkBinding = FragmentWatermarkBinding::inflate,
    override val viewModelClass: KClass<WatermarkViewModel> = WatermarkViewModel::class
) : BaseFragment<FragmentWatermarkBinding, WatermarkViewModel, WatermarkContract.WatermarkState, WatermarkContract.WatermarkEvent, WatermarkContract.WatermarkEffect>() {
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

    override fun onStateChange(state: WatermarkContract.WatermarkState) {
        when {
            state.isLoading -> {
                changeVisibilityOfProgressBar(true)
                errorResultConfig()
            }

            isIdleState(state) -> {
                errorResultConfig(true)
                changeVisibilityOfProgressBar()
            }

            else -> {
                changeVisibilityOfProgressBar()
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
        bundle.putParcelable(BUNDLE_MATERIAL, materialModel)
        bundle.putString(BUNDLE_TYPE, WATERMARK_TYPE)
        bundle.putString(BUNDLE_TITLE, title)
        val action =
            WatermarkFragmentDirections.actionWatermarkFragmentToPreviewMaterialNavGraph(bundle)
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
        navigateByRouteTitle(this@WatermarkFragment, HOME_ROUTE)
    }
}