package com.natiqhaciyef.prodocument.ui.view.main.files

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.databinding.FragmentFileBottomSheetOptionBinding
import com.natiqhaciyef.prodocument.ui.view.main.profile.model.ParamsUIModel
import com.natiqhaciyef.prodocument.ui.view.main.profile.params.preferences.adapter.ParamsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FileBottomSheetOptionFragment(
    private val material: MappedMaterialModel,
    var list: List<ParamsUIModel>
) : BottomSheetDialogFragment() {
    private var _binding: FragmentFileBottomSheetOptionBinding? = null
    private val binding: FragmentFileBottomSheetOptionBinding
        get() = _binding!!
    private var paramsAdapter: ParamsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFileBottomSheetOptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customMaterialPreviewConfig()
        recyclerViewConfig()
    }

    private fun customMaterialPreviewConfig() {
        with(binding) {
            customFilePreview.initFilePreview(file = material)
        }
    }

    private fun recyclerViewConfig() {
        paramsAdapter = ParamsAdapter(
            requireContext(),
            list.toMutableList(),
            com.natiqhaciyef.common.R.color.white
        )

        with(binding) {
            recyclerFileActionsView.adapter = paramsAdapter
            recyclerFileActionsView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            paramsAdapter?.action = {}
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        dialog.dismiss()
    }
}