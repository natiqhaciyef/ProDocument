package com.natiqhaciyef.prodocument.ui.view.main.profile.params.language

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.natiqhaciyef.common.model.LanguageModel
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.databinding.FragmentLanguageBinding
import com.natiqhaciyef.prodocument.ui.view.main.profile.params.language.adapter.LanguageAdapter
import dagger.hilt.android.AndroidEntryPoint
import org.intellij.lang.annotations.Language


@AndroidEntryPoint
class LanguageFragment(
    val onClickAction: (LanguageModel) -> Unit = {}
) : BottomSheetDialogFragment() {
    private var _binding: FragmentLanguageBinding? = null
    private val binding: FragmentLanguageBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLanguageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = LanguageAdapter(requireContext(), list)

        with(binding) {
            recyclerLanguage.adapter = adapter
            recyclerLanguage.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        adapter.onClick = onClickAction
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        dialog.dismiss()
    }

    companion object {
        var list = mutableListOf<LanguageModel>()
    }
}