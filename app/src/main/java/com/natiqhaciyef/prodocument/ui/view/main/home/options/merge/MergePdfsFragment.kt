package com.natiqhaciyef.prodocument.ui.view.main.home.options.merge

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.common.R
import com.natiqhaciyef.common.constants.EMPTY_STRING
import com.natiqhaciyef.common.constants.LINE
import com.natiqhaciyef.common.constants.SOMETHING_WENT_WRONG
import com.natiqhaciyef.common.constants.SPACE
import com.natiqhaciyef.common.constants.TWO
import com.natiqhaciyef.prodocument.databinding.FragmentMergePdfsBinding
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.core.base.ui.BaseRecyclerHolderStatefulFragment
import com.natiqhaciyef.prodocument.ui.util.BUNDLE_MATERIAL
import com.natiqhaciyef.prodocument.ui.util.BUNDLE_TITLE
import com.natiqhaciyef.prodocument.ui.util.BUNDLE_TYPE
import com.natiqhaciyef.uikit.manager.FileManager
import com.natiqhaciyef.prodocument.ui.util.NavigationUtil
import com.natiqhaciyef.prodocument.ui.util.NavigationUtil.navigateByRouteTitle
import com.natiqhaciyef.prodocument.ui.view.main.MainActivity
import com.natiqhaciyef.prodocument.ui.view.main.home.options.merge.contract.MergePdfContract
import com.natiqhaciyef.prodocument.ui.view.main.home.options.merge.viewmodel.MergePdfViewModel
import com.natiqhaciyef.uikit.adapter.FileItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

@AndroidEntryPoint
class MergePdfsFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMergePdfsBinding = FragmentMergePdfsBinding::inflate,
    override val viewModelClass: KClass<MergePdfViewModel> = MergePdfViewModel::class
) : BaseRecyclerHolderStatefulFragment<
        FragmentMergePdfsBinding, MergePdfViewModel, MappedMaterialModel, FileItemAdapter,
        MergePdfContract.MergePdfState, MergePdfContract.MergePdfEvent, MergePdfContract.MergePdfEffect>() {
    private val filesList = mutableListOf<MappedMaterialModel>()
    override var adapter: FileItemAdapter? = null
    private var resourceBundle: Bundle = bundleOf()

    private val fileRequestLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.let { intent ->
                    if (intent.data != null)
                        FileManager.readAndCreateFile(
                            activity = requireActivity(),
                            uri = intent.data!!
                        ) { file ->
                            configFileTitle(file.title)
                            filesList.add(file)
                            recyclerViewConfig(filesList)
                            configOfChangeFileList()
                            continueButtonEnabled()
                        }
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityConfigs()
        setFileListEmptyCheckConfig()
        setFilesCountConfigurations()

        with(binding) {
            addMoreFilesButton.setOnClickListener { FileManager.getFile(fileRequestLauncher) }
            mergeButton.setOnClickListener { mergeButtonEvent(filesList) }
            goBackIcon.setOnClickListener {
                navigateByRouteTitle(this@MergePdfsFragment, NavigationUtil.HOME_ROUTE)
            }
        }
    }

    override fun recyclerViewConfig(list: List<MappedMaterialModel>) {
        adapter = FileItemAdapter(filesList, requireContext().getString(R.string.merge_pdf))

        with(binding) {
            materialsRecyclerView.adapter = adapter
            materialsRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        adapter?.removeAction = { removeFileButtonClickAction(it.id) }
    }

    override fun onStateChange(state: MergePdfContract.MergePdfState) {
        when {
            state.isLoading -> {
                changeVisibilityOfProgressBar(true)
                errorResultConfig()
            }

            isIdleState(state) -> {
                errorResultConfig(true)
                changeVisibilityOfProgressBar()
            }

            else -> {
                errorResultConfig()
                changeVisibilityOfProgressBar()

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

    private fun errorResultConfig(isVisible: Boolean = false){
        with(binding){
            notFoundLayout.visibility = if (isVisible) View.VISIBLE else View.GONE
            uiLayout.visibility = if (isVisible) View.GONE else View.VISIBLE

            notFoundDescription.text = getString(com.natiqhaciyef.common.R.string.files_loading_error_description_result)
            notFoundTitle.text = SOMETHING_WENT_WRONG
        }
    }

    private fun activityConfigs() {
        (activity as MainActivity).binding.apply {
            materialToolbar.visibility = View.GONE
        }
    }

    private fun configFileTitle(title: String) {
        with(binding) {
            val totalTitle = usernameMergedTitle.text.toString()
            if (totalTitle != EMPTY_STRING && totalTitle != SPACE)
                usernameMergedTitle.setText("$totalTitle $LINE $title")
            else
                usernameMergedTitle.setText(title)
        }
    }

    private fun removeFileTitle(title: String) {
        with(binding.usernameMergedTitle) {
            var totalTitle = text.toString()
            if (totalTitle.endsWith(title)) {
                totalTitle = totalTitle.removeSuffix(title)
                totalTitle = totalTitle.removeSuffix(LINE + SPACE)
            } else {
                totalTitle = totalTitle.removePrefix(title)
                totalTitle = totalTitle.removePrefix(SPACE + LINE)
            }

            setText(totalTitle)
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
        val title = binding.usernameMergedTitle.text.toString()
        resourceBundle.putParcelable(BUNDLE_MATERIAL, mappedMaterialModel)
        resourceBundle.putString(BUNDLE_TITLE, title)
        resourceBundle.putString(BUNDLE_TYPE, MERGE_PDF)

        val action = MergePdfsFragmentDirections.actionMergePdfsFragmentToPreviewMaterialNavGraph(
            resourceBundle
        )
        navigate(action)
    }

    private fun removeFileButtonClickAction(id: String) {
        val file = filesList.first { it.id == id }
        removeFileTitle(file.title)
        filesList.remove(file)
        configOfChangeFileList()
    }

    private fun continueButtonEnabled() {
        binding.mergeButton.isEnabled = filesList.size >= TWO
    }

    companion object {
        const val MERGE_PDF = "MergePDF"
    }
}