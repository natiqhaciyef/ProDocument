package com.natiqhaciyef.prodocument.ui.view.main.modify

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.RenderEffect
import android.graphics.Shader
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ExperimentalGetImage
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import coil.load
import com.natiqhaciyef.common.constants.EMPTY_STRING
import com.natiqhaciyef.common.constants.SOMETHING_WENT_WRONG
import com.natiqhaciyef.common.constants.TWO_HUNDRED
import com.natiqhaciyef.common.constants.TWO_HUNDRED_NINETY_NINE
import com.natiqhaciyef.common.constants.ZERO
import com.natiqhaciyef.common.model.CRUDModel
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.store.AppStorePrefKeys.TITLE_COUNT_KEY
import com.natiqhaciyef.prodocument.databinding.FragmentModifyPdfBinding
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.ui.util.NavigationUtil.HOME_ROUTE
import com.natiqhaciyef.prodocument.ui.view.main.home.CustomMaterialOptionsBottomSheetFragment
import com.natiqhaciyef.prodocument.ui.view.main.home.options.watermark.CustomWatermarkAdderBottomSheetFragment
import com.natiqhaciyef.core.model.CategoryItem
import com.natiqhaciyef.core.store.AppStorePrefKeys.WATERMARK_EXPLANATION_KEY
import com.natiqhaciyef.prodocument.BuildConfig
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.uikit.manager.FileManager.createAndShareFile
import com.natiqhaciyef.uikit.manager.FileManager.getAddressOfFile
import com.natiqhaciyef.uikit.manager.FileManager.createSafePdfUriLoader
import com.natiqhaciyef.prodocument.ui.util.BUNDLE_LIST_MATERIAL
import com.natiqhaciyef.prodocument.ui.util.BUNDLE_MATERIAL
import com.natiqhaciyef.prodocument.ui.util.BUNDLE_TITLE
import com.natiqhaciyef.prodocument.ui.util.BUNDLE_TYPE
import com.natiqhaciyef.prodocument.ui.util.NavigationUtil
import com.natiqhaciyef.prodocument.ui.util.NavigationUtil.CAPTURE_IMAGE_TYPE
import com.natiqhaciyef.prodocument.ui.util.NavigationUtil.COMPRESS_TYPE
import com.natiqhaciyef.prodocument.ui.util.NavigationUtil.PROTECT_TYPE
import com.natiqhaciyef.prodocument.ui.util.NavigationUtil.SCAN_QR_TYPE
import com.natiqhaciyef.prodocument.ui.util.NavigationUtil.SPLIT_TYPE
import com.natiqhaciyef.prodocument.ui.util.NavigationUtil.WATERMARK_TYPE
import com.natiqhaciyef.prodocument.ui.util.NavigationUtil.navigateByRouteTitle
import com.natiqhaciyef.prodocument.ui.view.main.MainActivity
import com.natiqhaciyef.prodocument.ui.view.main.home.options.merge.MergePdfsFragment.Companion.MERGE_PDF
import com.natiqhaciyef.prodocument.ui.view.main.modify.contract.ModifyPdfContract
import com.natiqhaciyef.prodocument.ui.view.main.modify.viewmodel.ModifyPdfViewModel
import com.natiqhaciyef.uikit.alert.AlertDialogManager.createDynamicResultAlertDialog
import dagger.hilt.android.AndroidEntryPoint
import eightbitlab.com.blurview.RenderScriptBlur
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
    private var resultDescription: String = EMPTY_STRING

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data: ModifyPdfFragmentArgs by navArgs()
        material = data.resourceBundle.getParcelable(BUNDLE_MATERIAL)
        type = data.resourceBundle.getString(BUNDLE_TYPE)
        title = data.resourceBundle.getString(BUNDLE_TITLE)
        config()
        blurConfig()

        binding.apply {
            material?.let { file ->
                countTitle()
                resultDescription = resultTitleFilter(type ?: EMPTY_STRING)
                when (type) {
                    SCAN_QR_TYPE -> scanQrConfig(file)
                    CAPTURE_IMAGE_TYPE -> captureImageConfig(file)
                    PREVIEW_IMAGE -> previewImageConfig(file)
                    MERGE_PDF -> mergeConfig(file)
                    WATERMARK_TYPE -> watermarkConfig(file)
                    SPLIT_TYPE -> {
                        val list =
                            (data.resourceBundle.getParcelableArray(BUNDLE_LIST_MATERIAL) as Array<MappedMaterialModel>).toList()
                        splitConfig(list)
                    }

                    PROTECT_TYPE -> protectConfig(file)
                    COMPRESS_TYPE -> compressConfig(file)
                    null -> { /* create effect */
                    }

                    else -> {}
                }

                if (type != PROTECT_TYPE)
                    titleButtonChangeAction()
            }
        }
    }

    override fun onStateChange(state: ModifyPdfContract.ModifyPdfState) {
        when {
            state.isLoading -> {
                changeVisibilityOfProgressBar(true)
                errorResultConfig()
            }

            isIdleState(state) -> {
                changeVisibilityOfProgressBar()
                errorResultConfig(true)
            }

            else -> {
                errorResultConfig()
                changeVisibilityOfProgressBar()

                if (state.optionsList != null)
                    showBottomSheetDialog(state.optionsList!!)

                if (state.result != null)
                    saveButtonClickAction(result = state.result!!)
            }
        }
    }

    override fun onEffectUpdate(effect: ModifyPdfContract.ModifyPdfEffect) {
        when (effect) {
            is ModifyPdfContract.ModifyPdfEffect.CreateMaterialFailEffect -> {

            }
        }
    }


    private fun config() {
        (activity as MainActivity).binding.apply {
            materialToolbar.visibility = View.GONE
            bottomNavBar.visibility = View.GONE
        }

        binding.goBackIcon.setOnClickListener {
            navigateByRouteTitle(
                this@ModifyPdfFragment,
                HOME_ROUTE
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

    private fun errorResultConfig(isVisible: Boolean = false){
        with(binding){
            notFoundLayout.visibility = if (isVisible) View.VISIBLE else View.GONE
            uiLayout.visibility = if (isVisible) View.GONE else View.VISIBLE

            notFoundDescription.text = getString(com.natiqhaciyef.common.R.string.files_loading_error_description_result)
            notFoundTitle.text = SOMETHING_WENT_WRONG
        }
    }

    private fun scanQrConfig(material: MappedMaterialModel) {
        with(binding) {
            imageView.visibility = View.VISIBLE
            pdfView.visibility = View.GONE
            if (material.url.toString().isNotEmpty())
                imageView.load(material.url)
            else
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
            uriAddress =
                getAddressOfFile(BuildConfig.APPLICATION_ID, requireContext(), material.url)
                    ?: EMPTY_STRING.toUri()
            pdfView.createSafePdfUriLoader(uriAddress!!)
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
//            uriAddress = getAddressOfFile(
//                BuildConfig.APPLICATION_ID,
//                requireContext(),
//                mappedMaterialModel.url
//            ) ?: EMPTY_STRING.toUri()
            pdfView.createSafePdfUriLoader(mappedMaterialModel.url)

            val pdfParams = pdfView.layoutParams as ConstraintLayout.LayoutParams
            pdfParams.bottomMargin = ZERO
            val params = pdfTitleText.layoutParams as ConstraintLayout.LayoutParams
            params.endToStart = optionsIconButton.id

            saveButton.visibility = View.GONE
            pdfTitleText.setText(material?.title ?: EMPTY_STRING)
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
            pdfView.createSafePdfUriLoader(material.url)

            val params = pdfTitleText.layoutParams as ConstraintLayout.LayoutParams
            params.endToStart = optionsIconButton.id

            lifecycleScope.launch {
                if (!dataStore.readBoolean(requireContext(), WATERMARK_EXPLANATION_KEY)) {
                    blurView.visibility = View.VISIBLE
                    watermarkLayout.visibility = View.VISIBLE
                    blurConfig(true)
                }
            }

            understandButton.setOnClickListener {
                blurView.visibility = View.GONE
                watermarkLayout.visibility = View.GONE
                lifecycleScope.launch {
                    dataStore.saveBoolean(requireContext(), true, WATERMARK_EXPLANATION_KEY)
                }
            }

            saveButton.text = getString(com.natiqhaciyef.common.R.string.continue_)
            pdfTitleText.setText(title ?: material.title)
            modifyIconButton.visibility = View.GONE
            saveButton.setOnClickListener {
                saveButtonClickEvent(material)
            }

            optionsIconButton.setOnClickListener {
                showWatermarkBottomSheetDialog(
                    material = material,
                    title = title ?: EMPTY_STRING
                )
            }
        }
    }

    private fun splitConfig(materials: List<MappedMaterialModel>) {
        with(binding) {
            pdfView.visibility = View.VISIBLE
            imageView.visibility = View.GONE
            pdfView.createSafePdfUriLoader(materials.first().url)

            val params = pdfTitleText.layoutParams as ConstraintLayout.LayoutParams
            params.endToStart = optionsIconButton.id

            saveButton.text = getString(com.natiqhaciyef.common.R.string.continue_)
            pdfTitleText.setText(title ?: materials.first().title)
            modifyIconButton.visibility = View.GONE
            saveButton.setOnClickListener { saveButtonClickEvent(material) }

            optionsIconButton.setOnClickListener { getOptionsEvent() }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun protectConfig(material: MappedMaterialModel) {
        var isLocked = true
        with(binding) {
            pdfView.visibility = View.VISIBLE
            protectionIcon.visibility = View.VISIBLE
            blurView.visibility = View.VISIBLE
            pdfView.createSafePdfUriLoader(material.url)
            blurConfig(true)
            modifyIconButton.visibility = View.VISIBLE
            modifyIconButton.setImageResource(com.natiqhaciyef.common.R.drawable.modify_icon)

            protectionIcon.setOnClickListener {
                OpenLockProtectedFileBottomSheetFragment {
                    if (it == material.protectionKey) {
                        isLocked = false
                        blurView.visibility = View.GONE
                        protectionIcon.visibility = View.GONE
                        modifyIconButton.setImageResource(com.natiqhaciyef.common.R.drawable.lock_icon)
                    }
                }.show(
                    if (!isAdded) return@setOnClickListener else childFragmentManager,
                    OpenLockProtectedFileBottomSheetFragment::class.simpleName
                )
            }

            val params = pdfTitleText.layoutParams as ConstraintLayout.LayoutParams
            params.endToStart = optionsIconButton.id

            modifyIconButton.setOnClickListener {
                blurView.visibility = View.VISIBLE
                protectionIcon.visibility = View.VISIBLE
                optionsIconButton.visibility = View.VISIBLE
                isLocked = !isLocked

                if (isLocked) {
                    modifyIconButton.setImageResource(com.natiqhaciyef.common.R.drawable.modify_icon)
                    titleButtonChangeAction()
                }else {
                    modifyIconButton.setImageResource(com.natiqhaciyef.common.R.drawable.lock_icon)
                }
            }


            saveButton.text = getString(com.natiqhaciyef.common.R.string.save)
            pdfTitleText.setText(title ?: material.title)
            saveButton.setOnClickListener { saveButtonClickEvent(material) }

            optionsIconButton.setOnClickListener { getOptionsEvent() }
        }
    }

    private fun compressConfig(material: MappedMaterialModel) {
        with(binding) {
            pdfView.visibility = View.VISIBLE
            pdfView.createSafePdfUriLoader(material.url)

            val params = pdfTitleText.layoutParams as ConstraintLayout.LayoutParams
            params.endToStart = optionsIconButton.id

            saveButton.text = getString(com.natiqhaciyef.common.R.string.save)
            pdfTitleText.setText(title ?: material.title)
            modifyIconButton.visibility = View.VISIBLE
            saveButton.setOnClickListener { saveButtonClickEvent(material) }

            optionsIconButton.setOnClickListener { getOptionsEvent() }
        }
    }

    private fun mergeConfig(material: MappedMaterialModel) {
        with(binding) {
            pdfView.visibility = View.VISIBLE
            imageView.visibility = View.GONE
            pdfView.createSafePdfUriLoader(material.url)

            val params = pdfTitleText.layoutParams as ConstraintLayout.LayoutParams
            params.endToStart = optionsIconButton.id

            saveButton.text = getString(com.natiqhaciyef.common.R.string.save)
            pdfTitleText.setText(title ?: material.title)
            modifyIconButton.visibility = View.VISIBLE
            saveButton.setOnClickListener {
                // continue button event
            }

            optionsIconButton.setOnClickListener { getOptionsEvent() }
        }
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

    private fun saveButtonClickAction(result: CRUDModel) {
        val description = if (result.resultCode in TWO_HUNDRED..TWO_HUNDRED_NINETY_NINE)
            getString(
                com.natiqhaciyef.common.R.string.file_created_success_description,
                resultDescription
            )
        else
            getString(
                com.natiqhaciyef.common.R.string.file_created_failed_description,
                resultDescription
            )

        (requireActivity() as AppCompatActivity).createDynamicResultAlertDialog(
            title = result.message,
            description = description,
            buttonText = getString(com.natiqhaciyef.common.R.string.continue_),
            resultIconId = com.natiqhaciyef.common.R.drawable.success_result_type_icon
        ) { dialog ->
            NavigationUtil.navigateByActivityTitle(HOME_ROUTE, requireActivity())
            dialog.dismiss()
        }
    }

    private fun resultTitleFilter(title: String) = when (title) {
        SCAN_QR_TYPE -> SCANNED
        CAPTURE_IMAGE_TYPE -> CAPTURED
        PREVIEW_IMAGE -> PREVIEW
        WATERMARK_TYPE -> WATERMARK_INSERTED
        SPLIT_TYPE -> SPLIT
        PROTECT_TYPE -> PROTECTED
        COMPRESS_TYPE -> COMPRESSED
        else -> {
            EMPTY_STRING
        }
    }

    private fun saveButtonClickEvent(materialModel: MappedMaterialModel?) {
        materialModel?.let {
            viewModel
                .postEvent(
                    ModifyPdfContract
                        .ModifyPdfEvent
                        .CreateMaterialEvent(material = materialModel)
                )
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
                    inputMethodManager.hideSoftInputFromWindow(pdfTitleText.windowToken, ZERO)
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

    private fun blurConfig(isEnabled: Boolean = false) {
        val radius = if (isEnabled) 5f else 0f
        with(binding) {
            blurView.setupWith(root, RenderScriptBlur(requireContext()))
                .setBlurRadius(radius)
            blurView.outlineProvider = ViewOutlineProvider.BACKGROUND
            blurView.setClipToOutline(true)
        }
    }

    private fun shareFile(material: MappedMaterialModel) = createAndShareFile(
        applicationId = BuildConfig.APPLICATION_ID,
        material = material,
        isShare = true
    )


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    companion object {
        const val PREVIEW_IMAGE = "PreviewImage"
        private const val CAPTURED = "Captured"
        private const val SCANNED = "Scanned"
        private const val PREVIEW = "Previewed"
        private const val WATERMARK_INSERTED = "Watermark inserted"
        private const val SPLIT = "Split"
        private const val PROTECTED = "Protected"
        private const val COMPRESSED = "Compressed"
    }
}