package com.natiqhaciyef.prodocument.ui.view.scan

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.natiqhaciyef.common.R
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.common.model.mapped.MappedTokenModel
import com.natiqhaciyef.domain.worker.config.DOCX
import com.natiqhaciyef.domain.worker.config.JPEG
import com.natiqhaciyef.domain.worker.config.PDF
import com.natiqhaciyef.domain.worker.config.PNG
import com.natiqhaciyef.domain.worker.config.URL
import com.natiqhaciyef.prodocument.databinding.FragmentModifyPdfBinding
import com.natiqhaciyef.prodocument.ui.base.BaseFragment
import com.natiqhaciyef.prodocument.ui.custom.CustomMaterialBottomSheetFragment
import com.natiqhaciyef.prodocument.ui.model.CategoryItem
import com.natiqhaciyef.prodocument.ui.store.AppStorePrefKeys.TITLE_COUNT_KEY
import com.natiqhaciyef.prodocument.ui.store.AppStorePrefKeys.TOKEN_KEY
import com.natiqhaciyef.prodocument.ui.util.CameraReader.Companion.createAndShareFile
import com.natiqhaciyef.prodocument.ui.util.CameraReader.Companion.getAddressOfFile
import com.natiqhaciyef.prodocument.ui.util.PdfReader.createDefaultPdfUriLoader
import com.natiqhaciyef.prodocument.ui.view.scan.viewmodel.ModifyPdfViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ModifyPdfFragment : BaseFragment<FragmentModifyPdfBinding, ModifyPdfViewModel>(
    FragmentModifyPdfBinding::inflate,
    ModifyPdfViewModel::class
) {
    private var material: MappedMaterialModel? = null
    var uriAddress: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data: ModifyPdfFragmentArgs by navArgs()
        material = data.fileMaterial
        val shareOptions = listOf(
            CategoryItem(
                id = 1,
                title = requireActivity().getString(R.string.share_link),
                iconId = R.drawable.link_icon,
                size = null,
                type = URL,
                sizeType = null
            ),
            CategoryItem(
                id = 2,
                title = requireActivity().getString(R.string.share_pdf),
                iconId = R.drawable.pdf_icon,
                type = PDF,
                size = null,
                sizeType = null
            ),
            CategoryItem(
                id = 3,
                title = requireActivity().getString(R.string.share_word),
                iconId = R.drawable.word_icon,
                type = DOCX,
                size = null,
                sizeType = null
            ),
            CategoryItem(
                id = 4,
                title = requireActivity().getString(R.string.share_jpg),
                iconId = R.drawable.image_icon,
                type = JPEG,
                size = null,
                sizeType = null
            ),
            CategoryItem(
                id = 5,
                title = requireActivity().getString(R.string.share_png),
                iconId = R.drawable.image_icon,
                type = PNG,
                size = null,
                sizeType = null
            ),
        )


        binding.apply {
            material?.let {
                val uri = material!!.url
                countTitle()

                uriAddress = getAddressOfFile(
                    requireContext(),
                    uri
                ) ?: "".toUri()

                pdfView.createDefaultPdfUriLoader(requireContext(), uriAddress!!)
//                shareIconButton.setOnClickListener { sharePdf(uri) }

                titleButtonChangeAction()

                saveButton.setOnClickListener { saveButtonClickAction(materialModel = material!!) }
                optionsIconButton.setOnClickListener { showBottomSheetDialog(shareOptions) }
            }
        }
    }

    private fun showBottomSheetDialog(shareOptions: List<CategoryItem>) {
        CustomMaterialBottomSheetFragment.list = shareOptions.toMutableList()
        CustomMaterialBottomSheetFragment { type ->
            shareFile(uri = uriAddress ?: "".toUri(), fileType = type)
        }.show(
            childFragmentManager,
            CustomMaterialBottomSheetFragment::class.simpleName
        )
    }

    private fun saveButtonClickAction(materialModel: MappedMaterialModel) {
        binding.saveButton.setOnClickListener {
            // create file holder & collect token
//            getToken { token ->
//                viewModel?.createMaterial(
//                    token = token,
//                    material = materialModel
//                )
//            }
        }
    }

    private fun getToken(onSuccess: (MappedTokenModel) -> Unit = { }) = lifecycleScope.launch {
        val result = dataStore.readParcelableClassData(
            context = requireContext(),
            classType = MappedTokenModel::class.java,
            key = TOKEN_KEY
        )

        if (result != null) {
            onSuccess(result)
            return@launch
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

    private fun shareFile(uri: Uri, fileType: String) = createAndShareFile(
        fileType = fileType,
        urls = listOf(uri),
        isShare = true
    )


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}