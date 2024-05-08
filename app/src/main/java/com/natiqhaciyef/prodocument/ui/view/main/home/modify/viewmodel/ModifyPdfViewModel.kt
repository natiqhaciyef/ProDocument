package com.natiqhaciyef.prodocument.ui.view.main.home.modify.viewmodel

import android.app.Activity
import android.content.Context
import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.helpers.toJsonString
import com.natiqhaciyef.common.model.Status
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.ui.BaseViewModel
import com.natiqhaciyef.domain.usecase.MATERIAL_MODEL
import com.natiqhaciyef.domain.usecase.MATERIAL_TITLE
import com.natiqhaciyef.domain.usecase.MATERIAL_WATERMARK
import com.natiqhaciyef.domain.usecase.USER_EMAIL
import com.natiqhaciyef.domain.usecase.material.CreateMaterialUseCase
import com.natiqhaciyef.domain.usecase.material.watermark.WatermarkMaterialUseCase
import com.natiqhaciyef.prodocument.ui.view.main.MainActivity
import com.natiqhaciyef.prodocument.ui.view.main.home.modify.contract.ModifyPdfContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ModifyPdfViewModel @Inject constructor(
    private val createMaterialByIdUseCase: CreateMaterialUseCase,
    private val watermarkMaterialUseCase: WatermarkMaterialUseCase
) : BaseViewModel<ModifyPdfContract.ModifyPdfState, ModifyPdfContract.ModifyPdfEvent, ModifyPdfContract.ModifyPdfEffect>() {

    override fun onEventUpdate(event: ModifyPdfContract.ModifyPdfEvent) {
        when (event) {
            is ModifyPdfContract.ModifyPdfEvent.CreateMaterialEvent -> {
                createMaterial(event.email, event.material)
            }

            is ModifyPdfContract.ModifyPdfEvent.GetShareOptions -> {
                getShareOptionsState(context = event.context, activity = event.activity)
            }

            is ModifyPdfContract.ModifyPdfEvent.WatermarkMaterialEvent -> {
                watermarkMaterial(title = event.title, watermark = event.watermark, material = event.mappedMaterialModel)
            }
        }
    }

    private fun getShareOptionsState(context: Context, activity: Activity) {
        setBaseState(
            getCurrentBaseState().copy(
                isLoading = false,
                optionsList = (activity as MainActivity).getShareOptionsList(context)
            )
        )
    }

    private fun createMaterial(email: String, material: MappedMaterialModel) {
        if (email.isNotEmpty()) {
            val materialStr = material.toJsonString()
            val requestMap = hashMapOf(
                USER_EMAIL to email,
                MATERIAL_MODEL to materialStr
            )

            viewModelScope.launch {
                createMaterialByIdUseCase.operate(requestMap).collectLatest { result ->
                    when (result.status) {
                        Status.SUCCESS -> {
                            result.data?.let { data ->
                                setBaseState(
                                    getCurrentBaseState().copy(
                                        isLoading = false,
                                        result = data,
                                        optionsList = null
                                    )
                                )
                            }
                        }

                        Status.ERROR -> {
                            setBaseState(
                                getCurrentBaseState().copy(
                                    isLoading = false,
                                    result = null,
                                    optionsList = null
                                )
                            )
                            postEffect(
                                ModifyPdfContract.ModifyPdfEffect.CreateMaterialFailEffect(
                                    message = result.message,
                                    exception = result.exception
                                )
                            )
                        }

                        Status.LOADING -> {
                            setBaseState(
                                getCurrentBaseState().copy(
                                    isLoading = true,
                                    optionsList = null
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private fun watermarkMaterial(title: String, material: MappedMaterialModel, watermark: String) {
        val map = mutableMapOf<String, Any>()
        map[MATERIAL_TITLE] = title
        map[MATERIAL_WATERMARK] = watermark
        map[MATERIAL_MODEL] = material

        if (title.isNotEmpty() && watermark.isNotEmpty()) {
            viewModelScope.launch {
                watermarkMaterialUseCase.operate(map).collectLatest { result ->
                    when(result.status){
                        Status.SUCCESS -> {
                            result.data?.let { data ->
                                setBaseState(
                                    getCurrentBaseState().copy(
                                        isLoading = false,
                                        material = data,
                                        optionsList = null
                                    )
                                )
                            }
                        }

                        Status.ERROR -> {
                            setBaseState(
                                getCurrentBaseState().copy(
                                    isLoading = false,
                                    result = null,
                                    optionsList = null
                                )
                            )
                            postEffect(
                                ModifyPdfContract.ModifyPdfEffect.CreateMaterialFailEffect(
                                    message = result.message,
                                    exception = result.exception
                                )
                            )
                        }

                        Status.LOADING -> {
                            setBaseState(
                                getCurrentBaseState().copy(
                                    isLoading = true,
                                    optionsList = null
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    override fun getInitialState(): ModifyPdfContract.ModifyPdfState =
        ModifyPdfContract.ModifyPdfState()
}