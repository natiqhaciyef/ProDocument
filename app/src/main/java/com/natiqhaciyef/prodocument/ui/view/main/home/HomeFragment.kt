package com.natiqhaciyef.prodocument.ui.view.main.home

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.natiqhaciyef.prodocument.databinding.FragmentHomeBinding
import com.natiqhaciyef.prodocument.domain.model.mapped.MappedMaterialModel
import com.natiqhaciyef.prodocument.ui.base.BaseFragment
import com.natiqhaciyef.prodocument.ui.base.BaseNavigationDeepLink.SCAN_ROUTE
import com.natiqhaciyef.prodocument.ui.util.UiList
import com.natiqhaciyef.prodocument.ui.view.main.MainActivity
import com.natiqhaciyef.prodocument.ui.view.main.home.adapter.FileItemAdapter
import com.natiqhaciyef.prodocument.ui.view.main.home.adapter.MenuAdapter
import com.natiqhaciyef.prodocument.ui.view.main.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private val homeViewModel: HomeViewModel by viewModels()
    private var searchIconClick = false
    private lateinit var menuAdapter: MenuAdapter
    private lateinit var fileAdapter: FileItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).binding.bottomNavBar.visibility = View.VISIBLE
        
        menuAdapterConfig()
        fileAdapterConfig()

        with(binding) {
            topbarSearchIcon.setOnClickListener { searchIconClickAction() }
        }
    }

    private fun searchIconClickAction() {
        searchIconClick = !searchIconClick

        binding.apply {
            if (searchIconClick) {
                topbarImage.visibility = View.GONE
                topbarTitle.visibility = View.GONE
                topbarSearch.visibility = View.VISIBLE
            } else {
                topbarImage.visibility = View.VISIBLE
                topbarTitle.visibility = View.VISIBLE
                topbarSearch.visibility = View.GONE
            }
        }
    }

    private fun menuAdapterConfig() {
        menuAdapter = MenuAdapter(list = UiList.generateHomeMenuItemsList(requireContext()))
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
//        homeViewModel.fileState.observe(viewLifecycleOwner) { uiState ->
//            fileAdapter = FileItemAdapter(requireContext(), uiState.list)
//            searchFile(uiState.list)
//
//            fileAdapter.onClickAction = { materialId ->
//                // navigate to file details screen
//            }
//
//            binding.apply {
//                filesRecyclerView.adapter = fileAdapter
//                filesRecyclerView.layoutManager =
//                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
//            }
//        }
    }

    private fun searchFile(list: List<MappedMaterialModel>) {
        binding.apply {
            topbarSearch.doOnTextChanged { text, start, before, count ->
                text?.let {
                    list.filter { it.title.contains(text.toString()) }
                    fileAdapter.updateList(list)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}