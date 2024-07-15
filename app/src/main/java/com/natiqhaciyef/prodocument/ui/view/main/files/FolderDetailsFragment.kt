package com.natiqhaciyef.prodocument.ui.view.main.files

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ExperimentalGetImage
import androidx.core.os.bundleOf
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.natiqhaciyef.common.constants.EMPTY_STRING
import com.natiqhaciyef.common.model.ParamsUIModel
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.ui.BaseRecyclerHolderStatefulFragment
import com.natiqhaciyef.core.model.CategoryItem
import com.natiqhaciyef.prodocument.BuildConfig
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.databinding.FragmentFolderDetailsBinding
import com.natiqhaciyef.prodocument.ui.util.BUNDLE_ID
import com.natiqhaciyef.prodocument.ui.util.BUNDLE_MATERIAL
import com.natiqhaciyef.prodocument.ui.util.BUNDLE_TYPE
import com.natiqhaciyef.prodocument.ui.view.main.MainActivity
import com.natiqhaciyef.prodocument.ui.view.main.files.contract.FileContract
import com.natiqhaciyef.prodocument.ui.view.main.files.viewmodel.FileViewModel
import com.natiqhaciyef.prodocument.ui.view.main.home.CustomMaterialOptionsBottomSheetFragment
import com.natiqhaciyef.prodocument.ui.view.main.modify.ModifyPdfFragment
import com.natiqhaciyef.uikit.adapter.FileItemAdapter
import com.natiqhaciyef.uikit.alert.AlertDialogManager.createDynamicResultAlertDialog
import com.natiqhaciyef.uikit.manager.FileManager.createAndShareFile
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

@AndroidEntryPoint
class FolderDetailsFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFolderDetailsBinding = FragmentFolderDetailsBinding::inflate,
    override val viewModelClass: KClass<FileViewModel> = FileViewModel::class,
) : BaseRecyclerHolderStatefulFragment<
        FragmentFolderDetailsBinding, FileViewModel, Any, FileItemAdapter,
        FileContract.FileState, FileContract.FileEvent, FileContract.FileEffect>() {
    override var adapter: FileItemAdapter? = null
    private var resBundle = bundleOf()
    private var storedMaterial: MappedMaterialModel? = null
    private var params = listOf<ParamsUIModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityConfig()
        val data: FolderDetailsFragmentArgs by navArgs()
        resBundle = data.resourceBundle
        val folderId = resBundle.getString(BUNDLE_ID) ?: EMPTY_STRING
        getFilesInFolder(folderId)
    }

    override fun onStateChange(state: FileContract.FileState) {
        when {
            state.isLoading -> binding.uiLayout.loadingState(true)

            isIdleState(state) -> binding.uiLayout.errorState(true)

            else -> {
                binding.uiLayout.successState()

                if (state.list != null)
                    recyclerViewConfig(state.list!!)

                if (state.material != null)
                    fileClickAction(state.material!!)

                if (state.params != null) {
                    params = state.params!!
                    optionClickAction(state)
                }
            }
        }
    }

    private fun activityConfig() {
        (activity as MainActivity).also {
            with(it.binding) {
                bottomNavBar.visibility = View.GONE
                materialToolbar.apply {
                    navigationIcon = null
                    visibility = View.VISIBLE
                    setTitleToolbar(getString(com.natiqhaciyef.common.R.string.category_graph))
                    changeVisibility(View.VISIBLE)
                    changeAppIcon(com.natiqhaciyef.common.R.drawable.back_arrow_icon) {
                        onToolbarBackPressAction(bottomNavBar)
                    }
                    appIconVisibility(View.VISIBLE)
                    setVisibilitySearch(View.GONE)
                    setVisibilityOptionsMenu(View.GONE)
                    setVisibilityToolbar(View.VISIBLE)
                }
            }
        }
    }

    private fun onToolbarBackPressAction(bnw: BottomNavigationView) {
        bnw.visibility = View.VISIBLE
        navigate(R.id.filesFragment)
    }


    override fun recyclerViewConfig(list: List<Any>) {
        adapter = FileItemAdapter(
            list.toMutableList(),
            getString(com.natiqhaciyef.common.R.string.default_type),
            this
        )

        with(binding) {
            filesRecyclerView.adapter = adapter
            filesRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            adapter?.onFileClickAction = { fileClickEvent(it.id) }
            adapter?.optionAction = {
                storedMaterial = it
                optionClickEvent()
            }

            adapter?.bottomSheetMaterialDialogOpen = {
                showBottomSheetDialog(
                    it,
                    (requireActivity() as MainActivity).getShareOptionsList(context = requireContext())
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
            FileBottomSheetOptionFragment(this, material, params,
                onClickAction = {
                    holdCurrentState(state)
                    val folderId = resBundle.getString(BUNDLE_ID) ?: EMPTY_STRING
                    getFilesInFolder(folderId)
                }
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
            title = requireContext().getString(com.natiqhaciyef.common.R.string.remove_title_result),
            description = requireContext().getString(com.natiqhaciyef.common.R.string.remove_description_result),
            buttonText = requireContext().getString(com.natiqhaciyef.common.R.string.remove_button_result),
            resultIconId = com.natiqhaciyef.common.R.drawable.delete_bs_icon
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
        resBundle.putString(BUNDLE_TYPE, ModifyPdfFragment.PREVIEW_IMAGE)
        val action = FolderDetailsFragmentDirections.actionFolderDetailsFragmentToPreviewMaterialNavGraph(resBundle)
        navigate(action)
    }

    private fun getFilesInFolder(folderId: String){
        viewModel.postEvent(FileContract.FileEvent.GetMaterialsByFolderId(folderId))
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
}