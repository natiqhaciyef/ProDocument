package com.natiqhaciyef.prodocument.ui.view.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.natiqhaciyef.core.base.ui.BaseBottomSheetFragment
import com.natiqhaciyef.prodocument.databinding.FragmentCustomMaterialBottomSheetBinding
import com.natiqhaciyef.core.model.CategoryItem
import com.natiqhaciyef.uikit.adapter.ShareCategoryAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CustomMaterialOptionsBottomSheetFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCustomMaterialBottomSheetBinding =
        FragmentCustomMaterialBottomSheetBinding::inflate,
    override var onItemClickAction: (String) -> Unit = {},
) : BaseBottomSheetFragment<FragmentCustomMaterialBottomSheetBinding, String>() {
    private var adapter: ShareCategoryAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ShareCategoryAdapter(list)

        with(binding) {
            recyclerOptionsView.adapter = adapter
            recyclerOptionsView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            adapter?.onClickAction = onItemClickAction
        }
    }

    companion object {
        var list = mutableListOf<CategoryItem>()
    }
}