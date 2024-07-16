package com.natiqhaciyef.prodocument.ui.view.main.files

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import com.natiqhaciyef.common.constants.EMPTY_STRING
import com.natiqhaciyef.core.base.ui.BaseBottomSheetFragment
import com.natiqhaciyef.common.R
import com.natiqhaciyef.common.helpers.getNow
import com.natiqhaciyef.common.model.FolderType
import com.natiqhaciyef.domain.network.request.FolderRequest
import com.natiqhaciyef.prodocument.databinding.FragmentCreateFolderBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateFolderFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCreateFolderBinding = FragmentCreateFolderBinding::inflate,
    override var onItemClickAction: (FolderRequest) -> Unit = {}
) : BaseBottomSheetFragment<FragmentCreateFolderBinding, FolderRequest>() {
    private var folderModel: FolderRequest? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenTexts()
        with(binding) {
            saveButton.setOnClickListener {
                folderModel = FolderRequest(
                    title = fileTitleText.text.toString(),
                    description = filesDescriptionText.text.toString(),
                    icon = "folder_filled_icon",
                    type = FolderType.MIX.name,
                    createdDate = getNow()
                )

                onItemClickAction.invoke(folderModel!!)

                dialog?.dismiss()
            }
        }
    }

    private fun listenTexts(){
        with(binding){
            fileTitleText.doOnTextChanged { text, start, before, count ->
                saveButton.isEnabled = saveButtonEnabledConditions()
            }

            filesDescriptionText.doOnTextChanged { text, start, before, count ->
                saveButton.isEnabled = saveButtonEnabledConditions()
            }
        }
    }

    private fun saveButtonEnabledConditions() =
        binding.fileTitleText.text.toString() != EMPTY_STRING &&
                binding.filesDescriptionText.text.toString() != EMPTY_STRING
}