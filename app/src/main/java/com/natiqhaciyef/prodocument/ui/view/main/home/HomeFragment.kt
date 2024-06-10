package com.natiqhaciyef.prodocument.ui.view.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.natiqhaciyef.common.R
import com.natiqhaciyef.prodocument.databinding.FragmentHomeBinding
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.ui.util.NavigationManager.navigateByRouteTitle
import com.natiqhaciyef.prodocument.ui.util.BundleConstants.BUNDLE_MATERIAL
import com.natiqhaciyef.prodocument.ui.util.BundleConstants.BUNDLE_TYPE
import com.natiqhaciyef.prodocument.ui.util.UiList
import com.natiqhaciyef.prodocument.ui.view.main.MainActivity
import com.natiqhaciyef.prodocument.ui.view.main.home.adapter.FileItemAdapter
import com.natiqhaciyef.prodocument.ui.view.main.home.adapter.MenuAdapter
import com.natiqhaciyef.prodocument.ui.view.main.home.contract.HomeContract
import com.natiqhaciyef.prodocument.ui.view.main.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

@AndroidEntryPoint
class HomeFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBinding = FragmentHomeBinding::inflate,
    override val viewModelClass: KClass<HomeViewModel> = HomeViewModel::class
) : BaseFragment<FragmentHomeBinding, HomeViewModel, HomeContract.HomeUiState, HomeContract.HomeEvent, HomeContract.HomeEffect>() {
    private var resourceBundle = bundleOf()
    private lateinit var menuAdapter: MenuAdapter
    private lateinit var fileAdapter: FileItemAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        config()
    }

    override fun onStateChange(state: HomeContract.HomeUiState) {
        when {
            state.isLoading -> {
                changeVisibilityOfProgressBar(true)
            }

            else -> {
                changeVisibilityOfProgressBar()

                fileAdapterConfig(state.list)

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
            else -> {}
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

    private fun config() {
        (activity as MainActivity).also {
            it.binding.bottomNavBar.visibility = View.VISIBLE
            it.binding.materialToolbar.visibility = View.VISIBLE
            it.binding.appbarLayout.visibility = View.VISIBLE
            it.binding.materialToolbar.setTitleToolbar(getString(R.string.proscan))
            it.binding.materialToolbar.changeVisibility(View.VISIBLE)
            it.binding.materialToolbar.setVisibilityOptionsMenu(View.GONE)
            it.binding.materialToolbar.setVisibilitySearch(View.GONE)
            it.binding.materialToolbar.setVisibilityToolbar(View.VISIBLE)
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
            (activity as MainActivity).apply {
                binding.bottomNavBar.visibility = View.GONE
                binding.appbarLayout.visibility = View.GONE
            }
        }

        binding.apply {
            homeRecyclerMenubar.adapter = menuAdapter
            homeRecyclerMenubar.layoutManager =
                GridLayoutManager(requireContext(), 4, GridLayoutManager.VERTICAL, false)
            homeRecyclerMenubar.isScrollContainer = false
        }
    }

    private fun fileAdapterConfig(list: List<MappedMaterialModel>?) {
        list?.let {
            fileAdapter = FileItemAdapter(
                list.toMutableList(),
                requireContext().getString(R.string.default_type),
                this,
                requireContext()
            )

            fileAdapter.onClickAction = { materialId ->
                fileClickEvent(materialId)
            }

            binding.apply {
                filesRecyclerView.adapter = fileAdapter
                filesRecyclerView.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            }
        }
    }

    private fun fileClickEvent(materialId: String) {
        viewModel.postEvent(HomeContract.HomeEvent.GetMaterialById(id = materialId))
    }

    private fun fileClickAction(material: MappedMaterialModel) {
        resourceBundle.putParcelable(BUNDLE_MATERIAL, material)
        val action = HomeFragmentDirections.actionHomeFragmentToPreviewMaterialNavGraph(resourceBundle)
        navigate(action)
    }

    private fun recentFilesClickAction() {
        binding.rightArrowIcon.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToRecentFilesFragment()
            navigate(action)
        }
    }
}