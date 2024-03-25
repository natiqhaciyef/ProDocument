package com.natiqhaciyef.prodocument.ui.view.main.files

import android.os.Bundle
import android.view.View
import com.natiqhaciyef.prodocument.databinding.FragmentFilesBinding
import com.natiqhaciyef.prodocument.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilesFragment : BaseFragment<FragmentFilesBinding, FileViewModel>(
    FragmentFilesBinding::inflate,
    FileViewModel::class
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}