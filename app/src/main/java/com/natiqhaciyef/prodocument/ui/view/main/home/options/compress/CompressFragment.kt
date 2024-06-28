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
import com.natiqhaciyef.common.constants.ONE
import com.natiqhaciyef.common.constants.THREE
import com.natiqhaciyef.common.constants.TWO
import com.natiqhaciyef.common.model.Quality
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.ui.BaseFragment
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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        config()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onStateChange(state: CompressContract.CompressState) {
        when {
            state.isLoading -> {
                changeVisibilityOfProgressBar(true)
            }

            else -> {
                changeVisibilityOfProgressBar()

                if (state.material != null)
                    compressButtonClickAction(state.material!!)
            }
        }
    }

    override fun onEffectUpdate(effect: CompressContract.CompressEffect) {

    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun config() {
        val data: CompressFragmentArgs by navArgs()
        resourceBundle = data.resourceBundle
        radioGroupChooseAction()

        binding.compressButton.setOnClickListener { compressButtonClickEvent() }
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

    @SuppressLint("ResourceType")
    private fun radioButtonConfig() {
        with(binding) {
            highLevelRadioButton.id = ONE
            mediumLevelRadioButton.id = TWO
            lowLevelRadioButton.id = THREE
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun compressButtonClickEvent() {
        val material =
            resourceBundle.getParcelable(BUNDLE_MATERIAL, MappedMaterialModel::class.java)

        if (material != null && quality != null){
            viewModel
                .postEvent(
                    CompressContract.CompressEvent.CompressMaterialEvent(
                        material = material,
                        quality = quality!!
                    )
                )
        }
    }

    private fun compressButtonClickAction(material: MappedMaterialModel){
        resourceBundle.putParcelable(BUNDLE_MATERIAL, material)
        val action = CompressFragmentDirections.actionCompressFragmentToPreviewMaterialNavGraph(resourceBundle)
        navigate(action)
    }
}