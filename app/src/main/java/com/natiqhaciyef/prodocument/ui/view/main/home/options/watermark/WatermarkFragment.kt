package com.natiqhaciyef.prodocument.ui.view.main.home.options.watermark

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import coil.load
import com.natiqhaciyef.common.helpers.getNow
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.prodocument.databinding.FragmentWatermarkBinding
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.core.model.FileTypes.PDF
import com.natiqhaciyef.prodocument.ui.util.BaseNavigationDeepLink.HOME_ROUTE
import com.natiqhaciyef.prodocument.ui.util.BaseNavigationDeepLink.navigateByRouteTitle
import com.natiqhaciyef.prodocument.ui.util.BundleConstants.BUNDLE_MATERIAL
import com.natiqhaciyef.prodocument.ui.util.BundleConstants.BUNDLE_TITLE
import com.natiqhaciyef.prodocument.ui.util.BundleConstants.BUNDLE_TYPE
import com.natiqhaciyef.prodocument.ui.util.DefaultImplModels
import com.natiqhaciyef.prodocument.ui.view.main.home.options.watermark.contract.WatermarkContract
import com.natiqhaciyef.prodocument.ui.view.main.home.options.watermark.viewmodel.WatermarkViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID
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
                        readAndCreateFile(intent.data!!)
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        config()
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
//                    image = uri.toString().removePrefix("content://")
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
        material.type = type ?: PDF

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
        bundle.putParcelable(BUNDLE_MATERIAL, materialModel)
        bundle.putString(BUNDLE_TYPE, WATERMARK_TYPE)
        bundle.putString(BUNDLE_TITLE, title)
        val action = WatermarkFragmentDirections.actionWatermarkFragmentToPreviewMaterialNavGraph(bundle)
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
        navigateByRouteTitle(this@WatermarkFragment,HOME_ROUTE)
    }

    private fun getDefaultMockFile() = DefaultImplModels.mappedMaterialModel

    companion object {
        const val WATERMARK_TYPE = "Watermark"
    }
}