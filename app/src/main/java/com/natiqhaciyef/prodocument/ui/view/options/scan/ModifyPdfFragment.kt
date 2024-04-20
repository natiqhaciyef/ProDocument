package com.natiqhaciyef.prodocument.ui.view.options.scan

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.camera.core.ExperimentalGetImage
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.natiqhaciyef.common.R
import com.natiqhaciyef.common.helpers.loadImage
import com.natiqhaciyef.common.model.CRUDModel
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.prodocument.databinding.FragmentModifyPdfBinding
import com.natiqhaciyef.prodocument.ui.base.BaseFragment
import com.natiqhaciyef.prodocument.ui.custom.CustomMaterialBottomSheetFragment
import com.natiqhaciyef.prodocument.ui.model.CategoryItem
import com.natiqhaciyef.prodocument.ui.store.AppStorePrefKeys.TITLE_COUNT_KEY
import com.natiqhaciyef.prodocument.ui.util.CameraReader.Companion.createAndShareFile
import com.natiqhaciyef.prodocument.ui.util.CameraReader.Companion.getAddressOfFile
import com.natiqhaciyef.prodocument.ui.util.PdfReader.createDefaultPdfUriLoader
import com.natiqhaciyef.prodocument.ui.view.options.scan.contract.ModifyPdfContract
import com.natiqhaciyef.prodocument.ui.view.options.scan.viewmodel.ModifyPdfViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.reflect.KClass


@ExperimentalGetImage
@AndroidEntryPoint
class ModifyPdfFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentModifyPdfBinding = FragmentModifyPdfBinding::inflate,
    override val viewModelClass: KClass<ModifyPdfViewModel> = ModifyPdfViewModel::class
) : BaseFragment<FragmentModifyPdfBinding, ModifyPdfViewModel, ModifyPdfContract.ModifyPdfState, ModifyPdfContract.ModifyPdfEvent, ModifyPdfContract.ModifyPdfEffect>() {
    private var material: MappedMaterialModel? = null
    private var type: String? = null
    var uriAddress: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data: ModifyPdfFragmentArgs by navArgs()
        material = data.fileMaterial
        type = data.type


        binding.apply {
            material?.let {
                val uri = it.url
                countTitle()

                when (type) {
                    ScanFragment.SCAN_QR_TYPE -> {
                        imageView.visibility = View.VISIBLE
                        pdfView.visibility = View.GONE

                        imageView.loadImage(it.image)
                    }

                    CaptureImageFragment.CAPTURE_IMAGE_TYPE -> {
                        pdfView.visibility = View.VISIBLE
                        imageView.visibility = View.GONE
                        uriAddress = getAddressOfFile(requireContext(), uri) ?: "".toUri()
                        pdfView.createDefaultPdfUriLoader(requireContext(), uriAddress!!)
                    }
                }

                titleButtonChangeAction()

                saveButton.setOnClickListener {
                    saveButtonClickEvent(material)
                }
                optionsIconButton.setOnClickListener {
                    getOptionsEvent()
                }
            }
        }
    }

    override fun onStateChange(state: ModifyPdfContract.ModifyPdfState) {
        when {
            state.isLoading -> {
                changeVisibilityOfProgressBar()
            }

            else -> {
                changeVisibilityOfProgressBar(false)
                if (state.optionsList != null)
                    showBottomSheetDialog(state.optionsList!!)

                if (state.result != null)
                    saveButtonClickAction(state.result!!)
            }
        }
    }

    override fun onEffectUpdate(effect: ModifyPdfContract.ModifyPdfEffect) {
        when (effect) {
            is ModifyPdfContract.ModifyPdfEffect.CreateMaterialFailEffect -> {

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

    private fun getOptionsEvent() {
        viewModel.postEvent(ModifyPdfContract.ModifyPdfEvent.GetShareOptions(requireContext()))
    }

    private fun showBottomSheetDialog(shareOptions: List<CategoryItem>) {
        CustomMaterialBottomSheetFragment.list = shareOptions.toMutableList()
        CustomMaterialBottomSheetFragment { type ->
            material?.let {
                shareFile(it.copy(type = type))
            }
        }.show(
            childFragmentManager,
            CustomMaterialBottomSheetFragment::class.simpleName
        )
    }

    private fun saveButtonClickAction(result: CRUDModel) {
        // action after save file
    }

    private fun saveButtonClickEvent(materialModel: MappedMaterialModel?) {
        materialModel?.let {
            getEmail {
                viewModel.postEvent(ModifyPdfContract.ModifyPdfEvent.CreateMaterialEvent(email = it, material = materialModel))
            }
        }
    }

    private fun titleButtonChangeAction() {
        binding.apply {
            val params = pdfTitleText.layoutParams as ConstraintLayout.LayoutParams
            pdfTitleText.inputType = InputType.TYPE_NULL
            modifyIconButton.setOnClickListener {
                pdfTitleText.isEnabled = true
                pdfTitleText.inputType = InputType.TYPE_CLASS_TEXT

                saveTitleText.visibility = View.VISIBLE
                optionsIconButton.visibility = View.GONE
                modifyIconButton.visibility = View.GONE

                params.endToStart = saveTitleText.id
            }

            saveTitleText.setOnClickListener {
                pdfTitleText.inputType = InputType.TYPE_NULL
                saveTitleText.visibility = View.GONE
                optionsIconButton.visibility = View.VISIBLE
                modifyIconButton.visibility = View.VISIBLE
                pdfTitleText.clearFocus()
                params.endToStart = modifyIconButton.id

                material?.apply {
                    title = pdfTitleText.text.toString()
                }

                if (view != null) {
                    val inputMethodManager =
                        requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

                    // on below line hiding our keyboard.
                    inputMethodManager.hideSoftInputFromWindow(pdfTitleText.windowToken, 0)
                }
            }
        }
    }

    private fun countTitle() {
        lifecycleScope.launch {
            var number = dataStore.readInt(requireContext(), TITLE_COUNT_KEY)
            dataStore.saveInt(requireContext(), ++number, TITLE_COUNT_KEY)
            binding.pdfTitleText.setText(getString(R.string.title_count, number.toString()))
        }
    }

    private fun shareFile(material: MappedMaterialModel) = createAndShareFile(
        material = material,
        isShare = true
    )


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}