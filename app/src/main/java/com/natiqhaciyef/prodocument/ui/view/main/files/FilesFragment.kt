package com.natiqhaciyef.prodocument.ui.view.main.files

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.natiqhaciyef.common.R
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.prodocument.databinding.FragmentFilesBinding
import com.natiqhaciyef.prodocument.ui.base.BaseFragment
import com.natiqhaciyef.prodocument.ui.view.main.MainActivity
import com.natiqhaciyef.prodocument.ui.view.main.files.contract.FileContract
import com.natiqhaciyef.prodocument.ui.view.main.files.viewmodel.FileViewModel
import com.natiqhaciyef.prodocument.ui.view.main.home.HomeFragment
import com.natiqhaciyef.prodocument.ui.view.main.home.adapter.FileItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

@AndroidEntryPoint
class FilesFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFilesBinding = FragmentFilesBinding::inflate,
    override val viewModelClass: KClass<FileViewModel> = FileViewModel::class
) : BaseFragment<FragmentFilesBinding, FileViewModel, FileContract.FileState, FileContract.FileEvent, FileContract.FileEffect>() {
    private lateinit var fileAdapter: FileItemAdapter
    private var list: MutableList<MappedMaterialModel> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // collect state
        config()
        getFilesEvent()
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

    private fun getFilesEvent() {
        getEmail { email ->
            viewModel.postEvent(FileContract.FileEvent.GetAllMaterials(email))
        }
    }

    private fun recyclerViewConfig(list: MutableList<MappedMaterialModel>) {
        with(binding) {
            fileTotalAmountTitle.text =
                getString(R.string.total_file_amount_title, "${list.size}")
            fileAdapter =
                FileItemAdapter(list, requireContext().getString(R.string.scan_code))

            filesRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            filesRecyclerView.adapter = fileAdapter
            fileAdapter.onClickAction = {
                fileClickEvent(it)
            }
        }
    }

    private fun fileClickEvent(id: String) {
        getEmail { email ->
//            if (email.isNotEmpty())
//                viewModel.postEvent(FileContract.FileEvent.GetMaterialById(id = id, email = email))
//            else
                viewModel.postEvent(FileContract.FileEvent.GetMaterialById(id = id, email = "userEmail"))
        }
    }

    private fun fileClickAction(material: MappedMaterialModel) {
        val action = FilesFragmentDirections.actionFilesFragmentToPreviewMaterialNavGraph(material)
        navigate(action)
    }

    private fun config() {
        (activity as MainActivity).also {
            it.binding.bottomNavBar.visibility = View.VISIBLE
            it.binding.materialToolbar.setTitleToolbar(getString(R.string.files))
            it.binding.materialToolbar.changeVisibility(View.VISIBLE)
            it.binding.materialToolbar.setVisibilityOptionsMenu(View.VISIBLE)
            it.binding.materialToolbar.setVisibilitySearch(View.VISIBLE)
            it.binding.materialToolbar.listenSearchText { charSequence, i, i2, i3 ->
                list.filter { file -> file.title.contains(charSequence.toString()) }
                fileAdapter.updateList(list.toMutableList())
            }
        }
    }


}