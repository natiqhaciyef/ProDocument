package com.natiqhaciyef.prodocument.ui.view.main.files

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ExperimentalGetImage
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.natiqhaciyef.common.R
import com.natiqhaciyef.common.constants.ELEMENT_NOT_FOUND
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
import com.natiqhaciyef.prodocument.ui.util.BUNDLE_ID
import com.natiqhaciyef.prodocument.ui.util.BUNDLE_TYPE
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
        FragmentFilesBinding, FileViewModel, Any, FileItemAdapter,
        FileContract.FileState, FileContract.FileEvent, FileContract.FileEffect>() {
    private var params = listOf<ParamsUIModel>()
    private var resBundle = bundleOf()
    override var adapter: FileItemAdapter? = null
    private var list: MutableList<MappedMaterialModel> = mutableListOf()
    private var sortingTypeClick: Boolean = false
    private var storedMaterial: MappedMaterialModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // collect state
        config()
        getFilesAndFoldersEvent()
        fileFilter()
    }

    override fun onStateChange(state: FileContract.FileState) {
        when {
            state.isLoading -> binding.uiLayout.loadingState(true)

            isIdleState(state) -> binding.uiLayout.errorState(isVisible = true, isModified = false)

            else -> {
                binding.uiLayout.successState()
                if (
                    state.material == null
                    && !state.list.isNullOrEmpty()
                    && !state.params.isNullOrEmpty()
                    && state.result == null
                )
                    emptyResultConfig(true)
                else
                    emptyResultConfig()

                if (state.folders != null && state.list != null) {
                    val customList = mutableListOf<Any>()
                    customList.addAll(state.folders!!)
                    customList.addAll(state.list!!)
                    recyclerViewConfig(customList)
                } else if (state.list != null) {
                    holdCurrentState(state)
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
                    getFilesAndFoldersEvent()
            }
        }
    }

    override fun onEffectUpdate(effect: FileContract.FileEffect) {
        when (effect) {
            is FileContract.FileEffect.FilteredFileNotFoundEffect -> {
                Toast.makeText(requireContext(), ELEMENT_NOT_FOUND, Toast.LENGTH_SHORT).show()
            }

            is FileContract.FileEffect.FindMaterialByIdFailedEffect -> {}
        }
    }


    private fun emptyResultConfig(isVisible: Boolean = false) {
        with(binding) {
            if (isVisible) {
                uiLayout.customizeErrorState(
                    title = getString(R.string.nothing_modified_yet_result),
                    description = getString(R.string.files_not_inserted_yet_result),
                )
            } else {
                uiLayout.customizeErrorState(
                    title = getString(R.string.not_found_result_title),
                    description = getString(R.string.not_found_result_description)
                )
            }
        }
    }

    private fun getFilesAndFoldersEvent() {
        viewModel.postEvent(FileContract.FileEvent.GetAllMaterials)
        viewModel.postEvent(FileContract.FileEvent.GetAllFolders)
    }

    private fun fileFilter() {
        (activity as MainActivity).binding.materialToolbar.listenSearchText { charSequence, i, i2, i3 ->
            if (!charSequence.isNullOrEmpty())
                viewModel.postEvent(
                    FileContract.FileEvent.FileFilterEvent(list, charSequence.toString())
                )
            else
                getFilesAndFoldersEvent()
        }
    }

    override fun recyclerViewConfig(list: List<Any>) {
        with(binding) {
            fileTotalAmountTitle.text =
                getString(R.string.total_file_amount_title, "${list.size}")
            adapter = FileItemAdapter(
                list.toMutableList(),
                requireContext().getString(R.string.scan_code),
                this@FilesFragment
            )

            filesRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            filesRecyclerView.adapter = adapter
            adapter?.onFileClickAction = { fileClickEvent(it.id) }
            adapter?.optionAction = {
                storedMaterial = it
                optionClickEvent()
            }

            adapter?.onFolderClickAction = {
                resBundle.putString(BUNDLE_ID, it.id)
                viewModel.postEvent(FileContract.FileEvent.Clear)
                val action =
                    FilesFragmentDirections.actionFilesFragmentToFolderDetailsFragment(resBundle)
                navigate(action)
            }

            adapter?.bottomSheetMaterialDialogOpen = {
                showBottomSheetDialog(
                    it,
                    (requireActivity() as MainActivity).getShareOptionsList(context = requireContext())
                )
            }
        }
    }

    private fun config() {
        (activity as MainActivity).binding.apply {
            bottomNavBar.visibility = View.VISIBLE
            materialToolbar.visibility = View.VISIBLE
            materialToolbar.setTitleToolbar(getString(R.string.files))
            materialToolbar.changeVisibility(View.VISIBLE)
            materialToolbar.setVisibilityOptionsMenu(View.VISIBLE)
            materialToolbar.setVisibilitySearch(View.VISIBLE)
        }

        with(binding) {
            sortIcon.setOnClickListener { sortFilesClickEvent() }
            fabAddIcon.setOnClickListener {
                CreateFolderFragment {
                    viewModel.postEvent(FileContract.FileEvent.CreateFolder(it))
                }.show(
                    if (!isAdded) return@setOnClickListener else this@FilesFragment.childFragmentManager,
                    CreateFolderFragment::class.simpleName
                )
            }
        }
    }

    private fun optionClickEvent() {
        viewModel.postEvent(FileContract.FileEvent.GetAllFileParams(requireContext()))
    }

    private fun optionClickAction(state: FileContract.FileState) {
        // add bottom sheet here
        storedMaterial?.let { material ->
            FileBottomSheetOptionFragment(
                this, material, params,
                moveToFolderClickAction = {
                    resBundle.putParcelable(BUNDLE_MATERIAL, it)
                    val action =
                        FilesFragmentDirections.actionFilesFragmentToMoveToFolderFragment(resBundle)
                    navigate(action)
                },
                onClickAction = {
                    holdCurrentState(state)
                    getFilesAndFoldersEvent()
                },
            ) {
                removeFile(material)
            }.show(
                if (!isAdded) return else this.childFragmentManager,
                FileBottomSheetOptionFragment::class.simpleName
            )
        }
    }

    private fun removeFile(material: MappedMaterialModel) {
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
        resBundle.putParcelable(BUNDLE_MATERIAL, material)
        resBundle.putString(BUNDLE_TYPE, PREVIEW_IMAGE)
        val action = FilesFragmentDirections.actionFilesFragmentToPreviewMaterialNavGraph(resBundle)
        navigate(action)
    }

    private fun sortFilesClickEvent() {
        sortingTypeClick = !sortingTypeClick
        if (sortingTypeClick)
            viewModel.postEvent(FileContract.FileEvent.SortMaterials(list = list, type = A2Z))
        else
            viewModel.postEvent(FileContract.FileEvent.SortMaterials(list = list, type = Z2A))
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