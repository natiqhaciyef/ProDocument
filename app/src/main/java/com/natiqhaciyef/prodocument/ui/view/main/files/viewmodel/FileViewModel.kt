package com.natiqhaciyef.prodocument.ui.view.main.files.viewmodel

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.model.Status
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.ui.BaseViewModel
import com.natiqhaciyef.domain.usecase.material.GetAllMaterialsRemoteUseCase
import com.natiqhaciyef.domain.usecase.material.GetMaterialByIdRemoteUseCase
import com.natiqhaciyef.common.R
import com.natiqhaciyef.common.constants.EMPTY_STRING
import com.natiqhaciyef.prodocument.ui.view.main.files.FilesFragment
import com.natiqhaciyef.prodocument.ui.view.main.files.contract.FileContract
import com.natiqhaciyef.prodocument.ui.view.main.profile.model.FieldType
import com.natiqhaciyef.prodocument.ui.view.main.profile.model.ParamsUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FileViewModel @Inject constructor(
    private val getAllMaterialsRemoteUseCase: GetAllMaterialsRemoteUseCase,
    private val getMaterialByIdRemoteUseCase: GetMaterialByIdRemoteUseCase
) : BaseViewModel<FileContract.FileState, FileContract.FileEvent, FileContract.FileEffect>() {

    override fun onEventUpdate(event: FileContract.FileEvent) {
        when (event) {
            is FileContract.FileEvent.GetAllMaterials -> getAllMaterials() /* event.email */
            is FileContract.FileEvent.GetMaterialById -> {
                getMaterialById(event.id)
            }

            is FileContract.FileEvent.SortMaterials -> {
                sortMaterials(event.list, event.type)
            }

            is FileContract.FileEvent.FileFilterEvent -> {
                filterList(event.list, event.text)
            }

            is FileContract.FileEvent.GetAllFileParams -> {
                generateParams(event.context)
            }
        }
    }

    private fun getAllMaterials() {
        viewModelScope.launch {
            getAllMaterialsRemoteUseCase.invoke().collectLatest { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        if (result.data != null) {
                            setBaseState(getCurrentBaseState().copy(list = result.data!!))
                        }
                    }

                    Status.ERROR -> {
                        setBaseState(getCurrentBaseState().copy(isLoading = false))
                    }

                    Status.LOADING -> {
                        setBaseState(getCurrentBaseState().copy(isLoading = true))
                    }
                }
            }
        }
    }

    private fun getMaterialById(id: String) {
        viewModelScope.launch {
            getMaterialByIdRemoteUseCase.operate(id).collectLatest { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        if (result.data != null)
                            setBaseState(
                                getCurrentBaseState().copy(
                                    isLoading = false,
                                    material = result.data!!
                                )
                            )
                    }

                    Status.ERROR -> {
                        postEffect(
                            FileContract.FileEffect
                                .FindMaterialByIdFailedEffect(result.message, result.exception)
                        )
                        setBaseState(getCurrentBaseState().copy(isLoading = false))
                    }

                    Status.LOADING -> {
                        setBaseState(getCurrentBaseState().copy(isLoading = true))
                    }
                }
            }
        }
    }

    private fun sortMaterials(
        list: MutableList<MappedMaterialModel>,
        type: String
    ) {
        when (type) {
            FilesFragment.A2Z -> list.sortBy { it.title }
            FilesFragment.Z2A -> list.sortByDescending { it.title }
            else -> mutableListOf<MappedMaterialModel>()
        }
        setBaseState(
            getCurrentBaseState().copy(
                isLoading = false,
                list = list
            )
        )
    }

    private fun filterList(list: MutableList<MappedMaterialModel>, text: String) {
        if (list.any { it.title.startsWith(text) }) {
            setBaseState(
                getCurrentBaseState().copy(
                    isLoading = false,
                    list = list.filter { it.title.startsWith(text) })
            )
        }else{
            postEffect(FileContract.FileEffect.FilteredFileNotFoundEffect)
        }
    }

    private fun generateParams(ctx: Context){
        val paramsList = mutableListOf(
            ParamsUIModel(ctx.getString(R.string.save_to_device), FieldType.NAVIGATION,true, R.drawable.dowload_bs_icon),
            ParamsUIModel(ctx.getString(R.string.export_to), FieldType.NAVIGATION,true, R.drawable.export_bs_to),
            ParamsUIModel(EMPTY_STRING, FieldType.LINE),
            ParamsUIModel(ctx.getString(R.string.add_watermark), FieldType.NAVIGATION,true, R.drawable.watermark_bs_icon),
            ParamsUIModel(ctx.getString(R.string.add_digital_signature), FieldType.NAVIGATION,true, R.drawable.esign_bs_icon),
            ParamsUIModel(ctx.getString(R.string.split_pdf), FieldType.NAVIGATION,true, R.drawable.split_bs_icon),
            ParamsUIModel(ctx.getString(R.string.merge_pdf), FieldType.NAVIGATION,true, R.drawable.merge_bs_icon),
            ParamsUIModel(ctx.getString(R.string.protect_pdf), FieldType.NAVIGATION,true, R.drawable.password_bs_icon),
            ParamsUIModel(ctx.getString(R.string.compress_pdf), FieldType.NAVIGATION,true, R.drawable.compress_bs_icon),
            ParamsUIModel(EMPTY_STRING, FieldType.LINE),
            ParamsUIModel(ctx.getString(R.string.rename), FieldType.NAVIGATION,true, R.drawable.edit_bs_icon),
            ParamsUIModel(ctx.getString(R.string.move_to_folder), FieldType.NAVIGATION,true, R.drawable.folder_bs_icon),
            ParamsUIModel(ctx.getString(R.string.print), FieldType.NAVIGATION,true, R.drawable.print_bs_icon),
            ParamsUIModel(ctx.getString(R.string.delete), FieldType.NAVIGATION,true, R.drawable.delete_bs_icon),
        )

        setBaseState(getCurrentBaseState().copy(params = paramsList))
    }

    override fun getInitialState(): FileContract.FileState = FileContract.FileState()
}