package com.natiqhaciyef.prodocument.ui.view.main.home.options.compress

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.navigation.fragment.navArgs
import coil.load
import com.natiqhaciyef.common.constants.ONE
import com.natiqhaciyef.common.constants.SOMETHING_WENT_WRONG
import com.natiqhaciyef.common.constants.SPACE
import com.natiqhaciyef.common.constants.THREE
import com.natiqhaciyef.common.constants.TWO
import com.natiqhaciyef.common.model.Quality
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.databinding.FragmentCompressBinding
import com.natiqhaciyef.prodocument.ui.util.BUNDLE_MATERIAL
import com.natiqhaciyef.prodocument.ui.view.main.home.options.compress.contract.CompressContract
import com.natiqhaciyef.prodocument.ui.view.main.home.options.compress.viewmodel.CompressViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

@AndroidEntryPoint
class CompressFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCompressBinding = FragmentCompressBinding::inflate,
    override val viewModelClass: KClass<CompressViewModel> = CompressViewModel::class
) : BaseFragment<FragmentCompressBinding, CompressViewModel, CompressContract.CompressState, CompressContract.CompressEvent, CompressContract.CompressEffect>() {
    private var resourceBundle = bundleOf()
    private var quality: Quality? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data: CompressFragmentArgs by navArgs()
        resourceBundle = data.resBundle
        config()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onStateChange(state: CompressContract.CompressState) {
        when {
            state.isLoading -> {
                changeVisibilityOfProgressBar(true)
                errorResultConfig(false)
            }

            isIdleState(state) -> {
                changeVisibilityOfProgressBar()
                errorResultConfig()
            }

            else -> {
                errorResultConfig(false)
                changeVisibilityOfProgressBar()

                if (state.material != null)
                    compressButtonClickAction(state.material!!)
            }
        }
    }

    override fun onEffectUpdate(effect: CompressContract.CompressEffect) {

    }

    private fun config() {
        val material = resourceBundle.getParcelable<MappedMaterialModel>(BUNDLE_MATERIAL)
        radioGroupChooseAction()

        binding.compressButton.setOnClickListener { compressButtonClickEvent() }
        material?.let { previewPdfConfig(it) }
    }

    private fun changeVisibilityOfProgressBar(isVisible: Boolean = false) {
        if (isVisible) {
            binding.apply {
                progressBar.visibility = View.VISIBLE
                progressBar.isIndeterminate = true
            }
        } else {
            binding.apply {
                progressBar.visibility = View.GONE
                progressBar.isIndeterminate = false
            }
        }
    }

    private fun errorResultConfig(isVisible: Boolean = true){
        with(binding){
            notFoundLayout.visibility = if (isVisible) View.VISIBLE else View.GONE
            uiLayout.visibility = if (isVisible) View.GONE else View.VISIBLE

            notFoundDescription.text = getString(com.natiqhaciyef.common.R.string.files_loading_error_description_result)
            notFoundTitle.text = SOMETHING_WENT_WRONG
        }
    }


    private fun previewPdfConfig(material: MappedMaterialModel) {
        with(binding) {
            filePreviewImage.load(material.image)
            fileTitleText.text = material.title
            fileDetailsText.text = material.description
                ?: getString(com.natiqhaciyef.common.R.string.file_description_result)
        }
    }

    @SuppressLint("ResourceType")
    private fun radioButtonConfig() {
        with(binding) {
            highLevelRadioButton.id = ONE
            mediumLevelRadioButton.id = TWO
            lowLevelRadioButton.id = THREE
        }
    }

    private fun radioGroupChooseAction() {
        radioButtonConfig()
        with(binding) {
            compressQualityRadioGroup.setOnCheckedChangeListener { radioGroup, i ->
                quality = when (i) {
                    ONE -> { Quality.HIGH }

                    TWO -> { Quality.MEDIUM }

                    THREE -> { Quality.LOW }

                    else -> { Quality.STANDARD }
                }

                if (i in ONE..THREE)
                    compressButton.isEnabled = true
            }
        }
    }

    private fun compressButtonClickEvent() {
        val material =
            resourceBundle.getParcelable<MappedMaterialModel>(BUNDLE_MATERIAL)

        if (material != null && quality != null) {
            viewModel
                .postEvent(
                    CompressContract.CompressEvent.CompressMaterialEvent(
                        material = material,
                        quality = quality!!
                    )
                )
        }
    }

    private fun compressButtonClickAction(material: MappedMaterialModel) {
        resourceBundle.putParcelable(BUNDLE_MATERIAL, material)
        val action = CompressFragmentDirections
            .actionCompressFragmentToPreviewMaterialNavGraph(resourceBundle)
        navigate(action)
    }
}