package com.natiqhaciyef.prodocument.ui.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.api.Distribution.BucketOptions.Linear
import com.natiqhaciyef.prodocument.databinding.FragmentCustomMaterialBottomSheetBinding
import com.natiqhaciyef.prodocument.ui.model.CategoryItem
import com.natiqhaciyef.prodocument.ui.view.scan.adapter.ShareCategoryAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CustomMaterialBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentCustomMaterialBottomSheetBinding? = null
    private val binding: FragmentCustomMaterialBottomSheetBinding
        get() = _binding!!
    private var adapter: ShareCategoryAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCustomMaterialBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ShareCategoryAdapter(list)

        with(binding) {
            recyclerOptionsView.adapter = adapter
            recyclerOptionsView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        var list = mutableListOf<CategoryItem>()
    }
}