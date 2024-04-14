package com.natiqhaciyef.prodocument.ui.view.main.files

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.prodocument.databinding.FragmentFilesBinding
import com.natiqhaciyef.prodocument.ui.base.BaseFragment
import com.natiqhaciyef.prodocument.ui.base.BaseUIState
import com.natiqhaciyef.prodocument.ui.base.State
import com.natiqhaciyef.prodocument.ui.base.TotalUIState
import com.natiqhaciyef.prodocument.ui.view.main.files.event.FileEvent
import com.natiqhaciyef.prodocument.ui.view.main.files.viewmodel.FileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

@AndroidEntryPoint
class FilesFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFilesBinding = FragmentFilesBinding::inflate,
    override val viewModelClass: KClass<FileViewModel> = FileViewModel::class
) : BaseFragment<FragmentFilesBinding, FileViewModel, FileEvent>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // collect state

    }



    override fun onStateChange(state: State) {
        when(state.data){
            is TotalUIState.Success<*> -> {}
            is TotalUIState.Error -> {}
            is TotalUIState.Loading -> {}
            else -> {}
        }
    }
}