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
import com.natiqhaciyef.prodocument.ui.util.BundleConstants.BUNDLE_MATERIAL
import com.natiqhaciyef.prodocument.ui.view.main.MainActivity
import com.natiqhaciyef.prodocument.ui.view.main.files.contract.FileContract
import com.natiqhaciyef.prodocument.ui.view.main.files.viewmodel.FileViewModel
import com.natiqhaciyef.prodocument.ui.view.main.home.adapter.FileItemAdapter
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
            }
        }
    }

    override fun onEffectUpdate(effect: FileContract.FileEffect) {
        when(effect){
            is FileContract.FileEffect.FilteredFileNotFoundEffect -> { notFoundResult() }
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
                viewModel.postEvent(FileContract.FileEvent.FileFilterEvent(list, charSequence.toString()))
            else
                getFilesEvent()
        }
    }

    private fun recyclerViewConfig(list: MutableList<MappedMaterialModel>) {
        with(binding) {
            fileTotalAmountTitle.text =
                getString(R.string.total_file_amount_title, "${list.size}")
            fileAdapter =
                FileItemAdapter(list, requireContext().getString(R.string.scan_code), this@FilesFragment, requireContext())

            filesRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            filesRecyclerView.adapter = fileAdapter
            fileAdapter.onClickAction = { fileClickEvent(it) }
        }
    }

    private fun config() {
        (activity as MainActivity).also {
            it.binding.bottomNavBar.visibility = View.VISIBLE
            it.binding.appbarLayout.visibility = View.VISIBLE
            it.binding.materialToolbar.setTitleToolbar(getString(R.string.files))
            it.binding.materialToolbar.changeVisibility(View.VISIBLE)
            it.binding.materialToolbar.setVisibilityOptionsMenu(View.VISIBLE)
            it.binding.materialToolbar.setVisibilitySearch(View.VISIBLE)
        }

        with(binding) {
            sortIcon.setOnClickListener { sortFilesClickEvent() }
        }
    }


    private fun fileClickEvent(id: String) {
        getToken { email ->
            viewModel.postEvent(
                FileContract.FileEvent.GetMaterialById(
                    id = id,
                    email = "userEmail"
                )
            )
        }
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

    private fun notFoundResult(){
        with(binding){
            progressBar.visibility = View.GONE
            uiLayout.visibility = View.GONE
            notFoundLayout.visibility = View.VISIBLE
        }
    }

    companion object {
        const val A2Z = "Ascending"
        const val Z2A = "Descending"
    }
}