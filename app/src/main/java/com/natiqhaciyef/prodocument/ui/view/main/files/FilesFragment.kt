package com.natiqhaciyef.prodocument.ui.view.main.files

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.natiqhaciyef.common.R
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.prodocument.databinding.FragmentFilesBinding
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.core.model.CategoryItem
import com.natiqhaciyef.prodocument.ui.manager.FileManager.createAndShareFile
import com.natiqhaciyef.prodocument.ui.util.BUNDLE_MATERIAL
import com.natiqhaciyef.prodocument.ui.view.main.MainActivity
import com.natiqhaciyef.prodocument.ui.view.main.files.contract.FileContract
import com.natiqhaciyef.prodocument.ui.view.main.files.viewmodel.FileViewModel
import com.natiqhaciyef.prodocument.ui.view.main.home.CustomMaterialOptionsBottomSheetFragment
import com.natiqhaciyef.common.model.ParamsUIModel
import com.natiqhaciyef.uikit.adapter.FileItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

@AndroidEntryPoint
class FilesFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFilesBinding = FragmentFilesBinding::inflate,
    override val viewModelClass: KClass<FileViewModel> = FileViewModel::class
) : BaseFragment<FragmentFilesBinding, FileViewModel, FileContract.FileState, FileContract.FileEvent, FileContract.FileEffect>() {
    private var bundle = bundleOf()
    private lateinit var fileAdapter: FileItemAdapter
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
            }

            else -> {
                changeVisibilityOfProgressBar()

                if (state.list != null) {
                    list = state.list!!.toMutableList()
                    recyclerViewConfig(list)
                }

                if (state.material != null) {
                    fileClickAction(state.material!!)
                }

                if (state.params != null) {
                    optionClickAction(state.params!!)
                }
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

    private fun getFilesEvent() {
        getToken { token ->
            viewModel.postEvent(FileContract.FileEvent.GetAllMaterials)
        }
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

    private fun recyclerViewConfig(list: MutableList<MappedMaterialModel>) {
        with(binding) {
            fileTotalAmountTitle.text =
                getString(R.string.total_file_amount_title, "${list.size}")
            fileAdapter =
                FileItemAdapter(
                    list,
                    requireContext().getString(R.string.scan_code),
                    this@FilesFragment,
                    requireContext()
                )

            filesRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            filesRecyclerView.adapter = fileAdapter
            fileAdapter.onClickAction = { fileClickEvent(it.id) }
            fileAdapter.optionAction = {
                storedMaterial = it
                optionClickEvent()
            }

            fileAdapter.bottomSheetDialogOpen = {
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

    private fun optionClickAction(params: List<ParamsUIModel>) {
        // add bottom sheet here
        storedMaterial?.let {
            FileBottomSheetOptionFragment(it, params) {
                // INSERT: remove action
            }.show(
                if (!isAdded) return else this.childFragmentManager,
                FileBottomSheetOptionFragment::class.simpleName
            )
        }
    }


    private fun fileClickEvent(id: String) {
        viewModel.postEvent(
            FileContract.FileEvent.GetMaterialById(id = id)
        )
    }

    private fun fileClickAction(material: MappedMaterialModel) {
        bundle.putParcelable(BUNDLE_MATERIAL, material)
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
        material = material,
        isShare = true
    )


    companion object {
        const val A2Z = "Ascending"
        const val Z2A = "Descending"
    }
}