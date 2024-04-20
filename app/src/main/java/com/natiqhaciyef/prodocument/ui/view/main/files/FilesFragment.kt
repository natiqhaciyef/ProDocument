package com.natiqhaciyef.prodocument.ui.view.main.files

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.natiqhaciyef.prodocument.databinding.FragmentFilesBinding
import com.natiqhaciyef.prodocument.ui.base.BaseFragment
import com.natiqhaciyef.prodocument.ui.view.main.files.contract.FileContract
import com.natiqhaciyef.prodocument.ui.view.main.files.viewmodel.FileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

@AndroidEntryPoint
class FilesFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFilesBinding = FragmentFilesBinding::inflate,
    override val viewModelClass: KClass<FileViewModel> = FileViewModel::class
) : BaseFragment<FragmentFilesBinding, FileViewModel, FileContract.FileState, FileContract.FileEvent, FileContract.FileEffect>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // collect state

    }

    override fun onStateChange(state: FileContract.FileState) {
        when{
            state.isLoading -> {
                changeVisibilityOfProgressBar(true)
            }

            else -> {
                changeVisibilityOfProgressBar()

            }
        }
    }

    override fun onEffectUpdate(effect: FileContract.FileEffect) {

    }

    private fun changeVisibilityOfProgressBar(isVisible: Boolean = false) {
        if (isVisible) {
            binding.apply {
                uiLayout.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
                progressBar.isIndeterminate = true
            }
        } else {
            binding.apply {
                uiLayout.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                progressBar.isIndeterminate = false
            }
        }
    }
}