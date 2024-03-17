package com.natiqhaciyef.prodocument.ui.view.scan

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.natiqhaciyef.common.R
import com.natiqhaciyef.common.worker.config.PDF
import com.natiqhaciyef.prodocument.databinding.FragmentModifyPdfBinding
import com.natiqhaciyef.prodocument.ui.base.BaseFragment
import com.natiqhaciyef.prodocument.ui.store.AppStorePrefKeys.TITLE_COUNT_KEY
import com.natiqhaciyef.prodocument.ui.util.CameraReader.Companion.createAndShareFile
import com.natiqhaciyef.prodocument.ui.util.CameraReader.Companion.getAddressOfFile
import com.natiqhaciyef.prodocument.ui.util.PdfReader.createDefaultPdfUriLoader
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ModifyPdfFragment : BaseFragment<FragmentModifyPdfBinding>() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentModifyPdfBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data: ModifyPdfFragmentArgs by navArgs()
        val material = data.fileMaterial

        binding.apply {
            if (material != null) {
                val uri = material.url
                countTitle()

                val uriAddress = getAddressOfFile(
                    requireContext(),
                    uri
                ) ?: "".toUri()

                pdfView.createDefaultPdfUriLoader(requireContext(), uriAddress)
                shareIconButton.setOnClickListener { sharePdf(uri) }
                titleChangeAction()
            }
        }
    }


    private fun sharePdf(uri: String) = createAndShareFile(
        fileType = PDF,
        urls = listOf(uri),
        isShare = true
    )

    private fun saveButtonClickAction(){
        binding.saveButton.setOnClickListener {
            // create file holder
        }
    }

    private fun titleChangeAction() {
        binding.apply {
            val params = pdfTitleText.layoutParams as ConstraintLayout.LayoutParams
            pdfTitleText.doOnTextChanged { text, start, before, count ->
                saveTitleText.visibility = View.VISIBLE
                shareIconButton.visibility = View.GONE
                params.endToStart = saveTitleText.id
            }

            saveTitleText.setOnClickListener {
                saveTitleText.visibility = View.GONE
                shareIconButton.visibility = View.VISIBLE
                pdfTitleText.clearFocus()
                params.endToStart = shareIconButton.id

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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}