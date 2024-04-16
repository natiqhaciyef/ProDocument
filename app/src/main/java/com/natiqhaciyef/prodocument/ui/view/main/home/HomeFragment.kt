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
import com.natiqhaciyef.common.objects.MATERIAL_TOKEN_MOCK_KEY
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
            it.binding.materialToolbar.setTitleToolbar(getString(R.string.proscan))
            it.binding.materialToolbar.changeVisibility(View.VISIBLE)
        }
        viewModel.postEvent(HomeContract.HomeEvent.GetAllMaterials(MATERIAL_TOKEN_MOCK_KEY))
        menuAdapterConfig()
    }

    override fun onStateChange(state: HomeContract.HomeUiState) {
        when {
            state.isLoading -> {
                binding.progressBarIndicator.visibility = View.VISIBLE
                binding.progressBarIndicator.isIndeterminate = true
            }

            else -> {
                binding.progressBarIndicator.visibility = View.GONE
                binding.progressBarIndicator.isIndeterminate = false


                fileAdapterConfig(state.list)
            }
        }
    }

    override fun onEffectUpdate(effect: HomeContract.HomeEffect) {
        when(effect) {
            is HomeContract.HomeEffect.FindMaterialByIdFailedEffect -> { }
            is HomeContract.HomeEffect.MaterialListLoadingFailedEffect -> { }
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
            fileAdapter =
                FileItemAdapter(list.toMutableList(), requireContext().getString(R.string.scan_code))

            fileAdapter.onClickAction = { materialId ->
//                viewModel.postEvent(HomeContract.HomeEvent.GetMaterialById(id = materialId, token = ""))
            }

            binding.apply {
                filesRecyclerView.adapter = fileAdapter
                filesRecyclerView.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            }
        }
    }
}