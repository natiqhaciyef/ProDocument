package com.natiqhaciyef.prodocument.ui.view.main.home.options.protect

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.OpenableColumns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import coil.load
import com.natiqhaciyef.common.helpers.getNow
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.core.model.FileTypes
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.databinding.FragmentProtectFileBinding
import com.natiqhaciyef.prodocument.ui.util.BaseNavigationDeepLink
import com.natiqhaciyef.prodocument.ui.util.BundleConstants
import com.natiqhaciyef.prodocument.ui.util.DefaultImplModels
import com.natiqhaciyef.prodocument.ui.view.main.home.options.protect.contract.ProtectFileContract
import com.natiqhaciyef.prodocument.ui.view.main.home.options.protect.viewmodel.ProtectFileViewModel
import com.natiqhaciyef.prodocument.ui.view.main.home.options.watermark.WatermarkFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID
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
                        readAndCreateFile(intent.data!!)
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        config()
    }

    override fun onStateChange(state: ProtectFileContract.ProtectFileState) {
        when {
            state.isLoading -> { changeVisibilityOfProgressBar(true) }

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
            addFileButton.setOnClickListener { addFileButtonAction() }
            goBackIcon.setOnClickListener { goBackIconClickAction() }
        }
    }

    @SuppressLint("Range")
    private fun readAndCreateFile(uri: Uri) {
        val cursor = requireActivity()
            .contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val displayName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                val fileType = MimeTypeMap.getSingleton()
                    .getExtensionFromMimeType(requireContext().contentResolver.getType(uri))
                val file = createFileObject(
                    uri = uri,
                    title = displayName,
                    type = fileType,
                )
                val title = binding.usernameWatermarkTitle.text.toString()

                fileConfig(file)
                binding.continueButton.isEnabled = true
                binding.continueButton.setOnClickListener { continueButtonAction(file, title) }
            }
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

    private fun createFileObject(
        uri: Uri,
        title: String? = null,
        description: String? = null,
        image: String? = null,
        type: String? = null
    ): MappedMaterialModel {
        val material = getDefaultMockFile()
        material.id = "${UUID.randomUUID()}"
        material.url = uri
        material.title = title ?: ""
        material.description = description
        material.image = image ?: ""
        material.createdDate = getNow()
        material.type = type ?: FileTypes.PDF

        return material.copy()
    }

    private fun addFileButtonAction() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
            val uri = Uri.parse("content://com.android.externalstorage.documents/")
            putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri)
        }
        fileRequestLauncher.launch(intent)
    }

    private fun continueButtonAction(materialModel: MappedMaterialModel, title: String) {
        bundle.putParcelable(BundleConstants.BUNDLE_MATERIAL, materialModel)
        bundle.putString(BundleConstants.BUNDLE_TYPE, PROTECT_TYPE)
        bundle.putString(BundleConstants.BUNDLE_TITLE, title)
        val action = ProtectFileFragmentDirections.actionProtectFileFragmentToAddPasswordFileFragment(bundle)
        navigate(action)
    }

    private fun fileRemoveClickAction() {
        binding.apply {
            filePreviewObject.visibility = View.GONE
            continueButton.isEnabled = false
            addFileButton.visibility = View.VISIBLE
        }
    }

    private fun goBackIconClickAction(){
        BaseNavigationDeepLink.navigateByRouteTitle(
            this@ProtectFileFragment,
            BaseNavigationDeepLink.HOME_ROUTE
        )
    }

    private fun getDefaultMockFile() = DefaultImplModels.mappedMaterialModel

    companion object {
        const val PROTECT_TYPE = "ProtectPdf"
    }
}