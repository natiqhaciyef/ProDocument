package com.natiqhaciyef.prodocument.ui.view.main.home.options.merge

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.common.R
import com.natiqhaciyef.common.helpers.getNow
import com.natiqhaciyef.prodocument.databinding.FragmentMergePdfsBinding
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.core.model.FileTypes.PDF
import com.natiqhaciyef.prodocument.ui.manager.FileManager
import com.natiqhaciyef.prodocument.ui.util.BaseNavigationDeepLink
import com.natiqhaciyef.prodocument.ui.util.BaseNavigationDeepLink.navigateByRouteTitle
import com.natiqhaciyef.prodocument.ui.util.DefaultImplModels
import com.natiqhaciyef.prodocument.ui.view.main.home.adapter.FileItemAdapter
import com.natiqhaciyef.prodocument.ui.view.main.home.options.merge.contract.MergePdfContract
import com.natiqhaciyef.prodocument.ui.view.main.home.options.merge.viewmodel.MergePdfViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.reflect.KClass

@AndroidEntryPoint
class MergePdfsFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMergePdfsBinding = FragmentMergePdfsBinding::inflate,
    override val viewModelClass: KClass<MergePdfViewModel> = MergePdfViewModel::class
) : BaseFragment<FragmentMergePdfsBinding, MergePdfViewModel, MergePdfContract.MergePdfState, MergePdfContract.MergePdfEvent, MergePdfContract.MergePdfEffect>() {
    private val filesList = mutableListOf<MappedMaterialModel>()
    private var adapter: FileItemAdapter? = null

    private val fileRequestLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.let { intent ->
                    if (intent.data != null)
                        FileManager.readAndCreateFile(
                            activity = requireActivity(),
                            uri = intent.data!!
                        ) { file ->
                            filesList.add(file)
                            configOfChangeFileList()
                            continueButtonEnabled()
                        }
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = FileItemAdapter(
            filesList,
            requireContext().getString(R.string.merge_pdf)
        )

        with(binding) {
            setFileListEmptyCheckConfig()
            setFilesCountConfigurations()
            materialsRecyclerView.adapter = adapter
            materialsRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            addMoreFilesButton.setOnClickListener { FileManager.getFile(fileRequestLauncher) }
            mergeButton.setOnClickListener { mergeButtonEvent(filesList) }
            goBackIcon.setOnClickListener {
                navigateByRouteTitle(
                    this@MergePdfsFragment,
                    BaseNavigationDeepLink.HOME_ROUTE
                )
            }
            adapter?.removeAction = { removeFileButtonClickAction(it) }
        }
    }

    override fun onStateChange(state: MergePdfContract.MergePdfState) {
        when {
            state.isLoading -> {
                changeVisibilityOfProgressBar(true)
            }

            else -> {
                changeVisibilityOfProgressBar(false)
                if (state.mappedMaterialModel != null) {
                    mergeButtonAction(state.mappedMaterialModel!!)
                }
            }
        }
    }

    override fun onEffectUpdate(effect: MergePdfContract.MergePdfEffect) {
        when (effect) {
            is MergePdfContract.MergePdfEffect.MergeFailedEffect -> {}
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

    private fun setFilesCountConfigurations() {
        lifecycleScope.launch {
            binding.mergeDescriptionText.text =
                getString(R.string.merge_pdf_description, "${filesList.size}")
        }
    }

    private fun setFileListEmptyCheckConfig() {
        binding.materialsRecyclerView.visibility =
            if (filesList.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun configOfChangeFileList() {
        adapter?.list = filesList
        setFilesCountConfigurations()
        setFileListEmptyCheckConfig()
        adapter?.notifyDataSetChanged()
    }


    private fun mergeButtonEvent(list: List<MappedMaterialModel>) {
        val title = binding.usernameMergedTitle.text.toString()
        viewModel.postEvent(MergePdfContract.MergePdfEvent.MergeMaterialsEvent(title, list))
    }

    private fun mergeButtonAction(mappedMaterialModel: MappedMaterialModel) {
        // navigation to success page
    }

    private fun removeFileButtonClickAction(id: String) {
        filesList.removeIf { it.id == id }
        adapter?.list = filesList
        configOfChangeFileList()
    }

    private fun continueButtonEnabled() {
        binding.mergeButton.isEnabled = filesList.size >= 2
    }
}