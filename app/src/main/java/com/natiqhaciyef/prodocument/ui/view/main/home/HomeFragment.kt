package com.natiqhaciyef.prodocument.ui.view.main.home

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.natiqhaciyef.common.R
import com.natiqhaciyef.common.constants.EMPTY_STRING
import com.natiqhaciyef.common.constants.FOUR
import com.natiqhaciyef.prodocument.databinding.FragmentHomeBinding
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.core.base.ui.BaseRecyclerHolderStatefulFragment
import com.natiqhaciyef.core.model.FileTypes
import com.natiqhaciyef.prodocument.ui.util.NavigationUtil.navigateByRouteTitle
import com.natiqhaciyef.prodocument.ui.util.BUNDLE_MATERIAL
import com.natiqhaciyef.prodocument.ui.util.BUNDLE_TYPE
import com.natiqhaciyef.prodocument.ui.util.DefaultImplModels
import com.natiqhaciyef.prodocument.ui.util.NavigationUtil.SCAN_QR_TYPE
import com.natiqhaciyef.prodocument.ui.util.UiList
import com.natiqhaciyef.prodocument.ui.view.main.MainActivity
import com.natiqhaciyef.prodocument.ui.view.main.home.contract.HomeContract
import com.natiqhaciyef.prodocument.ui.view.main.home.viewmodel.HomeViewModel
import com.natiqhaciyef.prodocument.ui.view.main.modify.ModifyPdfFragment.Companion.PREVIEW_IMAGE
import com.natiqhaciyef.uikit.adapter.FileItemAdapter
import com.natiqhaciyef.uikit.adapter.MenuAdapter
import com.natiqhaciyef.uikit.manager.PermissionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

@ExperimentalGetImage
@AndroidEntryPoint
class HomeFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBinding = FragmentHomeBinding::inflate,
    override val viewModelClass: KClass<HomeViewModel> = HomeViewModel::class
) : BaseRecyclerHolderStatefulFragment<
        FragmentHomeBinding, HomeViewModel, MappedMaterialModel, FileItemAdapter,
        HomeContract.HomeUiState, HomeContract.HomeEvent, HomeContract.HomeEffect>() {
    private var resourceBundle = bundleOf()
    private lateinit var menuAdapter: MenuAdapter
    override var adapter: FileItemAdapter? = null
    private var imageUri: Uri? = null

    private val registerForGalleryPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted)
                startGalleryConfig()
        }
    private val galleryClickIntent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.data != null)
                intentAction.invoke(result.data!!)
        }
    private val intentAction: (Intent) -> Unit = { intent ->
        imageUri = intent.data
        if (imageUri != null) {
            resourceBundle.putString(BUNDLE_TYPE, SCAN_QR_TYPE)
            resourceBundle.putParcelable(
                BUNDLE_MATERIAL, DefaultImplModels.mappedMaterialModel.copy(
                    title = intent.dataString ?: EMPTY_STRING,
                    url = imageUri!!,
                    type = FileTypes.URL
                )
            )

            val action = HomeFragmentDirections
                .actionHomeFragmentToPreviewMaterialNavGraph(resourceBundle)
            navigate(action)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityConfig()
        fabGalleryConfig()
        cameraConfig()
    }

    override fun onStateChange(state: HomeContract.HomeUiState) {
        when {
            state.isLoading -> {
                changeVisibilityOfProgressBar(true)
            }

            else -> {
                changeVisibilityOfProgressBar()

                if (state.list != null)
                    recyclerViewConfig(state.list!!)

                if (state.material != null) {
                    // navigate to single file action
                    fileClickAction(state.material!!)
                }
            }
        }
    }

    override fun onEffectUpdate(effect: HomeContract.HomeEffect) {
        when (effect) {
            is HomeContract.HomeEffect.FindMaterialByIdFailedEffect -> {}
            is HomeContract.HomeEffect.MaterialListLoadingFailedEffect -> {}
        }
    }

    private fun changeVisibilityOfProgressBar(isVisible: Boolean = false) {
        if (isVisible) {
            binding.apply {
                progressBarIndicator.visibility = View.VISIBLE
                progressBarIndicator.isIndeterminate = true
            }
        } else {
            binding.apply {
                progressBarIndicator.visibility = View.GONE
                progressBarIndicator.isIndeterminate = false
            }
        }
    }

    private fun activityConfig() {
        (activity as MainActivity).binding.apply {
            bottomNavBar.visibility = View.VISIBLE
            materialToolbar.visibility = View.VISIBLE
            materialToolbar.setTitleToolbar(getString(R.string.proscan))
            materialToolbar.changeVisibility(View.VISIBLE)
            materialToolbar.setVisibilityOptionsMenu(View.GONE)
            materialToolbar.setVisibilitySearch(View.GONE)
            materialToolbar.setVisibilityToolbar(View.VISIBLE)
        }
        viewModel.postEvent(HomeContract.HomeEvent.GetAllMaterials)
        menuAdapterConfig()
        recentFilesClickAction()
    }

    private fun menuAdapterConfig() {
        menuAdapter =
            MenuAdapter(UiList.generateHomeMenuItemsList(requireContext()).toMutableList())
        menuAdapter.onClickAction = { route, type ->
            val customBundle = bundleOf()
            if (type != null)
                customBundle.putString(BUNDLE_TYPE, type)

            navigateByRouteTitle(this@HomeFragment, route, customBundle)
            (activity as MainActivity).binding.apply {
                bottomNavBar.visibility = View.GONE
                materialToolbar.visibility = View.GONE
            }
        }

        binding.apply {
            homeRecyclerMenubar.adapter = menuAdapter
            homeRecyclerMenubar.layoutManager =
                GridLayoutManager(requireContext(), FOUR, GridLayoutManager.VERTICAL, false)
            homeRecyclerMenubar.isScrollContainer = false
        }
    }


    override fun recyclerViewConfig(list: List<MappedMaterialModel>) {
        adapter = FileItemAdapter(
            list.toMutableList(),
            requireContext().getString(R.string.default_type),
            this,
            requireContext()
        )

        adapter?.onClickAction = { material ->
            fileClickEvent(material.id)
        }

        binding.apply {
            filesRecyclerView.adapter = adapter
            filesRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun fileClickEvent(materialId: String) {
        viewModel.postEvent(HomeContract.HomeEvent.GetMaterialById(id = materialId))
    }

    private fun fileClickAction(material: MappedMaterialModel) {
        resourceBundle.putParcelable(BUNDLE_MATERIAL, material)
        resourceBundle.putString(BUNDLE_TYPE, PREVIEW_IMAGE)
        val action =
            HomeFragmentDirections.actionHomeFragmentToPreviewMaterialNavGraph(resourceBundle)
        navigate(action)
    }

    private fun recentFilesClickAction() {
        binding.rightArrowIcon.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToRecentFilesFragment()
            navigate(action)
        }
    }

    @OptIn(ExperimentalGetImage::class)
    private fun fabGalleryConfig() {
        binding.fabGalleryIcon.setOnClickListener {
            PermissionManager.Builder(this@HomeFragment, false)
                .addPermissionLauncher(registerForGalleryPermission)
                .request(PermissionManager.Permission.createCustomPermission(Manifest.permission.READ_EXTERNAL_STORAGE))
                .checkPermission { startGalleryConfig() }
                .build()
        }
    }

    private fun startGalleryConfig() {
        val intent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryClickIntent.launch(intent)
    }

    private fun cameraConfig() {
        binding.fabCameraIcon.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToScanNavGraph()
            navigate(action)
            (activity as MainActivity).binding.apply {
                bottomNavBar.visibility = View.GONE
                materialToolbar.visibility = View.GONE
            }
        }
    }
}