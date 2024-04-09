package com.natiqhaciyef.prodocument.ui.view.main.home

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.natiqhaciyef.common.R
import com.natiqhaciyef.prodocument.databinding.FragmentHomeBinding
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.prodocument.ui.base.BaseFragment
import com.natiqhaciyef.prodocument.ui.util.UiList
import com.natiqhaciyef.prodocument.ui.view.main.MainActivity
import com.natiqhaciyef.prodocument.ui.view.main.home.adapter.FileItemAdapter
import com.natiqhaciyef.prodocument.ui.view.main.home.adapter.MenuAdapter
import com.natiqhaciyef.prodocument.ui.view.main.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(
    FragmentHomeBinding::inflate,
    HomeViewModel::class
) {
    private var searchIconClick = false
    private lateinit var menuAdapter: MenuAdapter
    private lateinit var fileAdapter: FileItemAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).binding.bottomNavBar.visibility = View.VISIBLE

        menuAdapterConfig()
        fileAdapterConfig()
    }

    private fun searchIconClickAction() {
        searchIconClick = !searchIconClick
    }

    private fun menuAdapterConfig() {
        menuAdapter = MenuAdapter(UiList.generateHomeMenuItemsList(requireContext()).toMutableList())
        menuAdapter.onClickAction = { route ->
            navigateByRouteTitle(route)
            (activity as MainActivity).binding.bottomNavBar.visibility = View.GONE
        }

        binding.apply {
            homeRecyclerMenubar.adapter = menuAdapter
            homeRecyclerMenubar.layoutManager =
                GridLayoutManager(requireContext(), 4, GridLayoutManager.VERTICAL, false)
            homeRecyclerMenubar.isScrollContainer = false
        }
    }

    private fun fileAdapterConfig() {
        viewModel?.fileState?.observe(viewLifecycleOwner) { uiState ->
            fileAdapter = FileItemAdapter(
                uiState.list.toMutableList(),
                requireContext().getString(R.string.scan_code)
            )
            searchFile(uiState.list.toMutableList())

            fileAdapter.onClickAction = { materialId ->
//                navigate to file details screen
            }

            binding.apply {
                filesRecyclerView.adapter = fileAdapter
                filesRecyclerView.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            }
        }
    }

    private fun searchFile(list: MutableList<MappedMaterialModel>) {
        binding.apply {
//            topbarSearch.doOnTextChanged { text, start, before, count ->
//                text?.let {
//                    list.filter { it.title.contains(text.toString()) }
//                    fileAdapter.updateList(list)
//                }
//            }
        }
    }
}