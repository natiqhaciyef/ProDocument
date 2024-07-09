package com.natiqhaciyef.prodocument.ui.view.main.files

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ExperimentalGetImage
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.natiqhaciyef.common.R
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.prodocument.databinding.FragmentFilesBinding
import com.natiqhaciyef.core.model.CategoryItem
import com.natiqhaciyef.uikit.manager.FileManager.createAndShareFile
import com.natiqhaciyef.prodocument.ui.util.BUNDLE_MATERIAL
import com.natiqhaciyef.prodocument.ui.view.main.MainActivity
import com.natiqhaciyef.prodocument.ui.view.main.files.contract.FileContract
import com.natiqhaciyef.prodocument.ui.view.main.files.viewmodel.FileViewModel
import com.natiqhaciyef.prodocument.ui.view.main.home.CustomMaterialOptionsBottomSheetFragment
import com.natiqhaciyef.common.model.ParamsUIModel
import com.natiqhaciyef.core.base.ui.BaseRecyclerHolderStatefulFragment
import com.natiqhaciyef.prodocument.BuildConfig
import com.natiqhaciyef.prodocument.ui.util.BUNDLE_TYPE
import com.natiqhaciyef.prodocument.ui.view.main.home.contract.HomeContract
import com.natiqhaciyef.prodocument.ui.view.main.modify.ModifyPdfFragment
import com.natiqhaciyef.prodocument.ui.view.main.modify.ModifyPdfFragment.Companion.PREVIEW_IMAGE
import com.natiqhaciyef.uikit.adapter.FileItemAdapter
import com.natiqhaciyef.uikit.alert.AlertDialogManager.createDynamicResultAlertDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

@AndroidEntryPoint
class FilesFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFilesBinding = FragmentFilesBinding::inflate,
    override val viewModelClass: KClass<FileViewModel> = FileViewModel::class
) : BaseRecyclerHolderStatefulFragment<
        FragmentFilesBinding, FileViewModel, MappedMaterialModel, FileItemAdapter,
        FileContract.FileState, FileContract.FileEvent, FileContract.FileEffect>() {
    private var params = listOf<ParamsUIModel>()
    private var bundle = bundleOf()
    override var adapter: FileItemAdapter? = null
    private var list: MutableList<MappedMaterialModel> = mutableListOf()
    private var sortingTypeClick: Boolean = false
    private var storedMaterial: MappedMaterialModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // collect state
        config()
        getFilesEvent()
        fileFilter()
    }

    override fun onStateChange(state: FileContract.FileState) {
        when {
            state.isLoading -> {
                changeVisibilityOfProgressBar(true)
                errorResultConfig()
            }

            state.material == null && state.list.isNullOrEmpty() && state.params.isNullOrEmpty() && state.result == null -> {
                errorResultConfig(true)
            }

            else -> {
                errorResultConfig()
                changeVisibilityOfProgressBar()

                if (state.list != null) {
                    recyclerViewConfig(state.list!!)
                }

                if (state.material != null) {
                    fileClickAction(state.material!!)
                }

                if (state.params != null) {
                    params = state.params!!
                    optionClickAction(state)
                }

                if (state.result != null)
                    getFilesEvent()
            }
        }
    }

    override fun onEffectUpdate(effect: FileContract.FileEffect) {
        when (effect) {
            is FileContract.FileEffect.FilteredFileNotFoundEffect -> {
                notFoundResult()
            }

            is FileContract.FileEffect.FindMaterialByIdFailedEffect -> {}
            else -> {}
        }
    }

    private fun changeVisibilityOfProgressBar(isVisible: Boolean = false) {
        if (isVisible) {
            binding.apply {
                uiLayout.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
                progressBar.isIndeterminate = true
                notFoundLayout.visibility = View.GONE
            }
        } else {
            binding.apply {
                uiLayout.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                progressBar.isIndeterminate = false
                notFoundLayout.visibility = View.GONE
            }
        }
    }

    private fun errorResultConfig(isVisible: Boolean = false){
        with(binding){
            notFoundLayout.visibility = if (isVisible) View.VISIBLE else View.GONE

            if (isVisible){
                notFoundDescription.text = getString(R.string.files_not_inserted_yet_result)
                notFoundTitle.text = getString(R.string.nothing_modified_yet_result)
            }else{
                notFoundDescription.text = getString(R.string.not_found_result_description)
                notFoundTitle.text = getString(R.string.not_found_result_title)
            }
        }
    }

    private fun getFilesEvent() {
        viewModel.postEvent(FileContract.FileEvent.GetAllMaterials)
    }

    private fun fileFilter() {
        (activity as MainActivity).binding.materialToolbar.listenSearchText { charSequence, i, i2, i3 ->
            if (!charSequence.isNullOrEmpty())
                viewModel.postEvent(
                    FileContract.FileEvent.FileFilterEvent(
                        list,
                        charSequence.toString()
                    )
                )
            else
                getFilesEvent()
        }
    }

    override fun recyclerViewConfig(list: List<MappedMaterialModel>) {
        with(binding) {
            fileTotalAmountTitle.text =
                getString(R.string.total_file_amount_title, "${list.size}")
            adapter = FileItemAdapter(
                    list.toMutableList(),
                    requireContext().getString(R.string.scan_code),
                    this@FilesFragment,
                    requireContext()
                )

            filesRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            filesRecyclerView.adapter = adapter
            adapter?.onClickAction = { fileClickEvent(it.id) }
            adapter?.optionAction = {
                storedMaterial = it
                optionClickEvent()
            }

            adapter?.bottomSheetDialogOpen = {
                showBottomSheetDialog(
                    it,
                    (requireActivity() as MainActivity).getShareOptionsList(context = requireContext())
                )
            }
        }
    }

    private fun config() {
        (activity as MainActivity).also {
            it.binding.bottomNavBar.visibility = View.VISIBLE
            it.binding.materialToolbar.visibility = View.VISIBLE
            it.binding.materialToolbar.setTitleToolbar(getString(R.string.files))
            it.binding.materialToolbar.changeVisibility(View.VISIBLE)
            it.binding.materialToolbar.setVisibilityOptionsMenu(View.VISIBLE)
            it.binding.materialToolbar.setVisibilitySearch(View.VISIBLE)
        }

        with(binding) {
            sortIcon.setOnClickListener { sortFilesClickEvent() }
        }
    }

    private fun optionClickEvent() {
        viewModel.postEvent(FileContract.FileEvent.GetAllFileParams(requireContext()))
    }

    private fun optionClickAction(state: FileContract.FileState) {
        // add bottom sheet here
        storedMaterial?.let { material ->
            FileBottomSheetOptionFragment(this , material, params,
                onClickAction = {
                    holdCurrentState(state)
                    getFilesEvent()
                }
            ) {
                removeFile(material)
            }.show(
                if (!isAdded) return else this.childFragmentManager,
                FileBottomSheetOptionFragment::class.simpleName
            )
        }
    }

    private fun removeFile(material: MappedMaterialModel){
        (requireActivity() as AppCompatActivity).createDynamicResultAlertDialog(
            title = requireContext().getString(R.string.remove_title_result),
            description = requireContext().getString(R.string.remove_description_result),
            buttonText = requireContext().getString(R.string.remove_button_result),
            resultIconId = R.drawable.delete_bs_icon
        ) {
            viewModel.postEvent(FileContract.FileEvent.RemoveMaterial(material.id))
            it.dismiss()
        }
    }

    private fun fileClickEvent(id: String) {
        viewModel.postEvent(FileContract.FileEvent.GetMaterialById(id = id))
    }

    @OptIn(ExperimentalGetImage::class)
    private fun fileClickAction(material: MappedMaterialModel) {
        bundle.putParcelable(BUNDLE_MATERIAL, material)
        bundle.putString(BUNDLE_TYPE, PREVIEW_IMAGE)
        val action = FilesFragmentDirections.actionFilesFragmentToPreviewMaterialNavGraph(bundle)
        navigate(action)
    }

    private fun sortFilesClickEvent() {
        sortingTypeClick = !sortingTypeClick
        if (sortingTypeClick)
            viewModel.postEvent(FileContract.FileEvent.SortMaterials(list = list, type = A2Z))
        else
            viewModel.postEvent(FileContract.FileEvent.SortMaterials(list = list, type = Z2A))
    }

    private fun notFoundResult() {
        with(binding) {
            progressBar.visibility = View.GONE
            uiLayout.visibility = View.GONE
            notFoundLayout.visibility = View.VISIBLE
        }
    }

    private fun showBottomSheetDialog(
        material: MappedMaterialModel,
        shareOptions: List<CategoryItem>
    ) {
        CustomMaterialOptionsBottomSheetFragment.list = shareOptions.toMutableList()
        CustomMaterialOptionsBottomSheetFragment { type ->
            shareFile(material.copy(type = type))
        }.show(
            childFragmentManager,
            CustomMaterialOptionsBottomSheetFragment::class.simpleName
        )
    }

    private fun shareFile(material: MappedMaterialModel) = this.createAndShareFile(
        applicationId = BuildConfig.APPLICATION_ID,
        material = material,
        isShare = true
    )


    companion object {
        const val A2Z = "Ascending"
        const val Z2A = "Descending"
    }
}