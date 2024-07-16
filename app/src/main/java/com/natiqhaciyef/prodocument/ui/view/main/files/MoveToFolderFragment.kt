package com.natiqhaciyef.prodocument.ui.view.main.files

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.ui.BaseRecyclerHolderStatefulFragment
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.databinding.FragmentMoveToFolderBinding
import com.natiqhaciyef.prodocument.ui.util.BUNDLE_MATERIAL
import com.natiqhaciyef.prodocument.ui.view.main.files.contract.FileContract
import com.natiqhaciyef.prodocument.ui.view.main.files.viewmodel.FileViewModel
import com.natiqhaciyef.uikit.adapter.FileItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass


@AndroidEntryPoint
class MoveToFolderFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMoveToFolderBinding = FragmentMoveToFolderBinding::inflate,
    override val viewModelClass: KClass<FileViewModel> = FileViewModel::class,
) : BaseRecyclerHolderStatefulFragment<FragmentMoveToFolderBinding, FileViewModel, Any, FileItemAdapter,
        FileContract.FileState, FileContract.FileEvent, FileContract.FileEffect>() {
    override var adapter: FileItemAdapter? = null
    private var material: MappedMaterialModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.postEvent(FileContract.FileEvent.GetAllFolders)
        val data: MoveToFolderFragmentArgs by navArgs()
        val resBundle = data.resourceBundle
        material = resBundle.getParcelable<MappedMaterialModel>(BUNDLE_MATERIAL)
    }

    override fun onStateChange(state: FileContract.FileState) {
        when {
            state.isLoading -> binding.uiLayout.loadingState(true)

            isIdleState(state) -> binding.uiLayout.errorState(true)

            else -> {
                binding.uiLayout.successState()

                if (state.folders != null)
                    recyclerViewConfig(state.folders!!)

                if (state.result != null)
                    resultAction()
            }
        }
    }

    override fun recyclerViewConfig(list: List<Any>) {
        adapter = FileItemAdapter(
            list.toMutableList(),
            getString(com.natiqhaciyef.common.R.string.default_type)
        )

        with(binding) {
            recyclerFolderView.adapter = adapter
            recyclerFolderView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            adapter?.onFolderClickAction = { f ->
                material?.let { m ->
                    m.folderId = f.id
                    viewModel.postEvent(FileContract.FileEvent.UpdateMaterial(m))
                }
            }
        }
    }

    private fun resultAction(){
        navigate(R.id.filesFragment)

        viewModel.postEvent(FileContract.FileEvent.GetAllFolders)
        viewModel.postEvent(FileContract.FileEvent.GetAllMaterials)
    }
}