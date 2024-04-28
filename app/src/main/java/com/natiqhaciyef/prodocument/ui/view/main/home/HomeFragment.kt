package com.natiqhaciyef.prodocument.ui.view.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.natiqhaciyef.common.R
import com.natiqhaciyef.prodocument.databinding.FragmentHomeBinding
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.common.objects.USER_EMAIL_MOCK_KEY
import com.natiqhaciyef.prodocument.ui.base.BaseFragment
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
    private lateinit var menuAdapter: MenuAdapter
    private lateinit var fileAdapter: FileItemAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).also {
            it.binding.bottomNavBar.visibility = View.VISIBLE
            it.binding.materialToolbar.visibility = View.VISIBLE
            it.binding.appbarLayout.visibility = View.VISIBLE
            it.binding.materialToolbar.setTitleToolbar(getString(R.string.proscan))
            it.binding.materialToolbar.changeVisibility(View.VISIBLE)
            it.binding.materialToolbar.setVisibilityOptionsMenu(View.GONE)
            it.binding.materialToolbar.setVisibilitySearch(View.GONE)
        }
        viewModel.postEvent(HomeContract.HomeEvent.GetAllMaterials(USER_EMAIL_MOCK_KEY))
        menuAdapterConfig()
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


    private fun menuAdapterConfig() {
        menuAdapter =
            MenuAdapter(UiList.generateHomeMenuItemsList(requireContext()).toMutableList())
        menuAdapter.onClickAction = { route ->
            navigateByRouteTitle(route)
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
            fileAdapter = FileItemAdapter(list.toMutableList(), requireContext().getString(R.string.scan_code), this, requireContext())

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
        getEmail { email ->
//            if (email.isNotEmpty())
//                viewModel.postEvent(HomeContract.HomeEvent.GetMaterialById(id = materialId, email = email))
//            else
                viewModel.postEvent(HomeContract.HomeEvent.GetMaterialById(id = materialId, email = "userEmail"))
        }
    }

    private fun fileClickAction(material: MappedMaterialModel) {
        val action = HomeFragmentDirections.actionHomeFragmentToPreviewMaterialNavGraph(material)
        navigate(action)
    }
}