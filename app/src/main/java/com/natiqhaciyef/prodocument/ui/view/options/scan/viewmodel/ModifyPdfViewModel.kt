package com.natiqhaciyef.prodocument.ui.view.options.scan.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.R
import com.natiqhaciyef.common.helpers.toJsonString
import com.natiqhaciyef.common.model.CRUDModel
import com.natiqhaciyef.common.model.Status
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.common.model.mapped.MappedTokenModel
import com.natiqhaciyef.domain.usecase.MATERIAL_MODEL
import com.natiqhaciyef.domain.usecase.MATERIAL_TOKEN
import com.natiqhaciyef.domain.usecase.material.CreateMaterialUseCase
import com.natiqhaciyef.domain.worker.config.JPEG
import com.natiqhaciyef.domain.worker.config.PDF
import com.natiqhaciyef.domain.worker.config.PNG
import com.natiqhaciyef.domain.worker.config.URL
import com.natiqhaciyef.prodocument.ui.base.BaseUIState
import com.natiqhaciyef.prodocument.ui.base.BaseViewModel
import com.natiqhaciyef.prodocument.ui.model.CategoryItem
import com.natiqhaciyef.prodocument.ui.view.options.scan.contract.ModifyPdfContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ModifyPdfViewModel @Inject constructor(
    private val createMaterialByIdUseCase: CreateMaterialUseCase
) : BaseViewModel<ModifyPdfContract.ModifyPdfState, ModifyPdfContract.ModifyPdfEvent, ModifyPdfContract.ModifyPdfEffect>() {

    override fun onEventUpdate(event: ModifyPdfContract.ModifyPdfEvent) {
        when(event){
            is ModifyPdfContract.ModifyPdfEvent.CreateMaterialEvent -> {
                createMaterial(event.token, event.material)
            }

            is ModifyPdfContract.ModifyPdfEvent.GetShareOptions -> {
                getShareOptions(context = event.context)
            }

            else -> {}
        }
    }

    private fun getShareOptions(context: Context){
        setBaseState(getCurrentBaseState().copy(isLoading = false, optionsList = listOf(
            CategoryItem(
                id = 1,
                title = context.getString(R.string.share_link),
                iconId = R.drawable.link_icon,
                size = null,
                type = URL,
                sizeType = null
            ),
            CategoryItem(
                id = 2,
                title = context.getString(R.string.share_pdf),
                iconId = R.drawable.pdf_icon,
                type = PDF,
                size = null,
                sizeType = null
            ),
            CategoryItem(
                id = 3,
                title = context.getString(R.string.share_jpg),
                iconId = R.drawable.image_icon,
                type = JPEG,
                size = null,
                sizeType = null
            ),
            CategoryItem(
                id = 4,
                title = context.getString(R.string.share_png),
                iconId = R.drawable.image_icon,
                type = PNG,
                size = null,
                sizeType = null
            ),
        )))
    }

    private fun createMaterial(token: MappedTokenModel, material: MappedMaterialModel) {
        if (!token.uid.isNullOrEmpty()) {
            val materialStr = material.toJsonString()
            val requestMap = hashMapOf(
                MATERIAL_TOKEN to token.uid!!,
                MATERIAL_MODEL to materialStr
            )

            viewModelScope.launch {
                createMaterialByIdUseCase.operate(requestMap).collectLatest { result ->
                    when (result.status) {
                        Status.SUCCESS -> {
                            result.data?.let { data ->
                                setBaseState(getCurrentBaseState().copy(
                                    isLoading = false,
                                    result = data,
                                    optionsList = null
                                ))
                            }
                        }

                        Status.ERROR -> {
                            setBaseState(getCurrentBaseState().copy(isLoading = false, result = null, optionsList = null))
                            postEffect(ModifyPdfContract.ModifyPdfEffect.CreateMaterialFailEffect(
                                message = result.message,
                                exception = result.exception
                            ))
                        }

                        Status.LOADING -> {
                            setBaseState(getCurrentBaseState().copy(isLoading = true, optionsList = null))
                        }
                    }
                }
            }
        }
    }

    override fun getInitialState(): ModifyPdfContract.ModifyPdfState = ModifyPdfContract.ModifyPdfState()
}