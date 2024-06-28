package com.natiqhaciyef.prodocument.ui.view.main.home.recent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.common.R
import com.natiqhaciyef.common.model.mapped.MappedTokenModel
import com.natiqhaciyef.core.store.AppStorePrefKeys.TOKEN_KEY
import com.natiqhaciyef.prodocument.databinding.FragmentRecentFilesBinding
import com.natiqhaciyef.prodocument.ui.view.main.MainActivity
import com.natiqhaciyef.prodocument.ui.view.main.home.adapter.FileItemAdapter
import com.natiqhaciyef.prodocument.ui.view.main.home.recent.contract.RecentFilesContract
import com.natiqhaciyef.prodocument.ui.view.main.home.recent.viewmodel.RecentFilesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.reflect.KClass


@AndroidEntryPoint
class RecentFilesFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentRecentFilesBinding = FragmentRecentFilesBinding::inflate,
    override val viewModelClass: KClass<RecentFilesViewModel> = RecentFilesViewModel::class
) : BaseFragment<FragmentRecentFilesBinding, RecentFilesViewModel, RecentFilesContract.RecentFilesState, RecentFilesContract.RecentFilesEvent, RecentFilesContract.RecentFilesEffect>() {
    private var list: List<MappedMaterialModel> = listOf()
    private var fileAdapter: FileItemAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityConfig()
        getMaterialsEvent()
    }

    override fun onStateChange(state: RecentFilesContract.RecentFilesState) {
        when {
            state.isLoading -> {}
            else -> {
                if (state.list != null) {
                    list = state.list!!
                    adapterConfig()
                }
            }
        }
    }

    override fun onEffectUpdate(effect: RecentFilesContract.RecentFilesEffect) {

    }

    private fun activityConfig() {
        (activity as MainActivity).also {
            it.binding.bottomNavBar.visibility = View.GONE
            it.binding.materialToolbar.visibility = View.VISIBLE
            it.binding.materialToolbar.visibility = View.VISIBLE
            it.binding.materialToolbar.setTitleToolbar(getString(com.natiqhaciyef.common.R.string.recent_files))
            it.binding.materialToolbar.changeVisibility(View.VISIBLE)
            it.binding.materialToolbar.appIconVisibility(View.GONE)
            it.binding.materialToolbar.setVisibilityOptionsMenu(View.GONE)
            it.binding.materialToolbar.setVisibilitySearch(View.VISIBLE)
            it.binding.materialToolbar.setVisibilityToolbar(View.VISIBLE)
        }
    }

    private fun getMaterialsEvent() {
        lifecycleScope.launch {
            val token = dataStore.readParcelableClassData(requireContext(), MappedTokenModel::class.java, TOKEN_KEY)
            viewModel.postEvent(RecentFilesContract.RecentFilesEvent.GetAllMaterials)
        }
    }

    private fun adapterConfig() {
        fileAdapter =
            FileItemAdapter(
                dataList = list.toMutableList(),
                type = requireContext().getString(R.string.default_type),
                fragment = this,
                context = requireContext()
            )

        binding.filesRecyclerView.adapter = fileAdapter
        binding.filesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }
}