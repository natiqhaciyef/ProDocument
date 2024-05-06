package com.natiqhaciyef.prodocument.ui.view.main.home.modify

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
import coil.load
import com.natiqhaciyef.common.model.CRUDModel
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.prodocument.databinding.FragmentModifyPdfBinding
import com.natiqhaciyef.prodocument.ui.base.BaseFragment
import com.natiqhaciyef.prodocument.ui.base.BaseNavigationDeepLink.HOME_ROUTE
import com.natiqhaciyef.prodocument.ui.custom.CustomMaterialOptionsBottomSheetFragment
import com.natiqhaciyef.prodocument.ui.custom.CustomWatermarkAdderBottomSheetFragment
import com.natiqhaciyef.prodocument.ui.model.CategoryItem
import com.natiqhaciyef.prodocument.ui.store.AppStorePrefKeys.TITLE_COUNT_KEY
import com.natiqhaciyef.prodocument.ui.util.BundleConstants.BUNDLE_LIST_MATERIAL
import com.natiqhaciyef.prodocument.ui.util.BundleConstants.BUNDLE_MATERIAL
import com.natiqhaciyef.prodocument.ui.util.BundleConstants.BUNDLE_TITLE
import com.natiqhaciyef.prodocument.ui.util.BundleConstants.BUNDLE_TYPE
import com.natiqhaciyef.prodocument.ui.util.CameraReader.Companion.createAndShareFile
import com.natiqhaciyef.prodocument.ui.util.CameraReader.Companion.getAddressOfFile
import com.natiqhaciyef.prodocument.ui.util.PdfReader.createDefaultPdfUriLoader
import com.natiqhaciyef.prodocument.ui.view.main.MainActivity
import com.natiqhaciyef.prodocument.ui.view.main.home.modify.contract.ModifyPdfContract
import com.natiqhaciyef.prodocument.ui.view.main.home.modify.viewmodel.ModifyPdfViewModel
import com.natiqhaciyef.prodocument.ui.view.main.home.options.scan.CaptureImageFragment
import com.natiqhaciyef.prodocument.ui.view.main.home.options.scan.ScanFragment
import com.natiqhaciyef.prodocument.ui.view.main.home.options.split.SplitFragment.Companion.SPLIT_TYPE
import com.natiqhaciyef.prodocument.ui.view.main.home.options.watermark.WatermarkFragment.Companion.WATERMARK_TYPE
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
    private var title: String? = null
    private var uriAddress: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data: ModifyPdfFragmentArgs by navArgs()
        material = data.resourceBundle.getParcelable(BUNDLE_MATERIAL)
        type = data.resourceBundle.getString(BUNDLE_TYPE)
        title = data.resourceBundle.getString(BUNDLE_TITLE)
        config()


        binding.apply {
            material?.let { file ->
                countTitle()

                when (type) {
                    ScanFragment.SCAN_QR_TYPE -> {
                        scanQrConfig(file)
                    }

                    CaptureImageFragment.CAPTURE_IMAGE_TYPE -> {
                        captureImageConfig(file)
                    }

                    PREVIEW_IMAGE -> {
                        previewImageConfig(file)
                    }

                    WATERMARK_TYPE -> {
                        watermarkConfig(file)
                    }

                    SPLIT_TYPE ->{
                        val list = (data.resourceBundle.getParcelableArray(BUNDLE_LIST_MATERIAL) as Array<MappedMaterialModel>).toList()
                        splitConfig(list)
                    }

                    null -> { /* create effect */ }

                    else -> {}
                }

                titleButtonChangeAction()
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

                saveButtonClickAction(material = state.material, result = state.result)
            }
        }
    }

    override fun onEffectUpdate(effect: ModifyPdfContract.ModifyPdfEffect) {
        when (effect) {
            is ModifyPdfContract.ModifyPdfEffect.CreateMaterialFailEffect -> {

            }
            else -> {}
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

    private fun scanQrConfig(material: MappedMaterialModel) {
        with(binding) {
            imageView.visibility = View.VISIBLE
            pdfView.visibility = View.GONE
            imageView.load(material.image)
            saveButton.setOnClickListener {
                saveButtonClickEvent(material)
            }

            optionsIconButton.setOnClickListener { getOptionsEvent() }
        }
    }

    private fun captureImageConfig(material: MappedMaterialModel) {
        with(binding) {
            pdfView.visibility = View.VISIBLE
            imageView.visibility = View.GONE
            uriAddress = getAddressOfFile(requireContext(), material.url) ?: "".toUri()
            pdfView.createDefaultPdfUriLoader(uriAddress!!)
            saveButton.setOnClickListener {
                saveButtonClickEvent(material)
            }

            optionsIconButton.setOnClickListener { getOptionsEvent() }
        }
    }

    private fun previewImageConfig(mappedMaterialModel: MappedMaterialModel) {
        with(binding) {
            pdfView.visibility = View.VISIBLE
            imageView.visibility = View.GONE
            uriAddress = getAddressOfFile(requireContext(), mappedMaterialModel.url) ?: "".toUri()
            pdfView.createDefaultPdfUriLoader(uriAddress!!)

            val pdfParams = pdfView.layoutParams as ConstraintLayout.LayoutParams
            pdfParams.bottomMargin = 0
            val params = pdfTitleText.layoutParams as ConstraintLayout.LayoutParams
            params.endToStart = optionsIconButton.id

            saveButton.visibility = View.GONE
            pdfTitleText.setText(material?.title ?: "")
            modifyIconButton.visibility = View.GONE
            saveButton.setOnClickListener {
                saveButtonClickEvent(material)
            }

            optionsIconButton.setOnClickListener { getOptionsEvent() }
        }
    }

    private fun watermarkConfig(material: MappedMaterialModel) {
        with(binding) {
            pdfView.visibility = View.VISIBLE
            imageView.visibility = View.GONE
            pdfView.createDefaultPdfUriLoader(material.url)

            val params = pdfTitleText.layoutParams as ConstraintLayout.LayoutParams
            params.endToStart = optionsIconButton.id


            saveButton.text = getString(com.natiqhaciyef.common.R.string.continue_)
            pdfTitleText.setText(title ?: material.title)
            modifyIconButton.visibility = View.GONE
            saveButton.setOnClickListener {
                // continue button event
            }

            optionsIconButton.setOnClickListener { showWatermarkBottomSheetDialog(material = material, title = title ?: "") }
        }
    }

    private fun splitConfig(materials: List<MappedMaterialModel>){
        with(binding) {
            pdfView.visibility = View.VISIBLE
            imageView.visibility = View.GONE
            pdfView.createDefaultPdfUriLoader(materials.first().url)

            val params = pdfTitleText.layoutParams as ConstraintLayout.LayoutParams
            params.endToStart = optionsIconButton.id

            saveButton.text = getString(com.natiqhaciyef.common.R.string.continue_)
            pdfTitleText.setText(title ?: materials.first().title)
            modifyIconButton.visibility = View.GONE
            saveButton.setOnClickListener {
                // continue button event
            }

            optionsIconButton.setOnClickListener { getOptionsEvent() }
        }
    }

    private fun config() {
        (activity as MainActivity).binding.apply {
            appbarLayout.visibility = View.GONE
            bottomNavBar.visibility = View.GONE
        }

        binding.goBackIcon.setOnClickListener { navigateByRouteTitle(HOME_ROUTE) }
    }

    private fun getOptionsEvent() {
        viewModel.postEvent(
            ModifyPdfContract.ModifyPdfEvent.GetShareOptions(
                requireContext(),
                (requireActivity() as MainActivity)
            )
        )
    }

    private fun showBottomSheetDialog(shareOptions: List<CategoryItem>) {
        CustomMaterialOptionsBottomSheetFragment.list = shareOptions.toMutableList()
        CustomMaterialOptionsBottomSheetFragment { type ->
            material?.let {
                shareFile(it.copy(type = type))
            }
        }.show(
            childFragmentManager,
            CustomMaterialOptionsBottomSheetFragment::class.simpleName
        )
    }

    private fun showWatermarkBottomSheetDialog(title: String, material: MappedMaterialModel) {
        CustomWatermarkAdderBottomSheetFragment(
            cancelButtonCLickAction = {},
            continueButtonCLickAction = { watermark ->
                // watermark event
                viewModel.postEvent(
                    ModifyPdfContract.ModifyPdfEvent.WatermarkMaterialEvent(
                        title = title,
                        mappedMaterialModel = material,
                        watermark = watermark
                    )
                )
            }
        ).show(
            childFragmentManager,
            CustomWatermarkAdderBottomSheetFragment::class.simpleName
        )
    }

    private fun saveButtonClickAction(result: CRUDModel? = null, material: MappedMaterialModel? = null) {
        // action after save file
        println(material)
    }

    private fun saveButtonClickEvent(materialModel: MappedMaterialModel?) {
        materialModel?.let {
            getEmail {
                viewModel.postEvent(
                    ModifyPdfContract.ModifyPdfEvent.CreateMaterialEvent(
                        email = it,
                        material = materialModel
                    )
                )
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
            binding.pdfTitleText.setText(
                getString(
                    com.natiqhaciyef.common.R.string.title_count,
                    number.toString()
                )
            )
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


    companion object {
        const val PREVIEW_IMAGE = "PreviewImage"
    }
}