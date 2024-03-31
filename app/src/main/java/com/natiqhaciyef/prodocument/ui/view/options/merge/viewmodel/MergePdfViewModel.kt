package com.natiqhaciyef.prodocument.ui.view.options.merge.viewmodel

import androidx.lifecycle.MutableLiveData
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.prodocument.ui.base.BaseViewModel
import com.natiqhaciyef.prodocument.ui.util.DefaultImplModels
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MergePdfViewModel @Inject constructor(

) : BaseViewModel() {
    val filesModel = MutableLiveData<MutableList<MappedMaterialModel>>(mutableListOf())

    fun getDefaultMockFile() = DefaultImplModels.mappedMaterialModel
}