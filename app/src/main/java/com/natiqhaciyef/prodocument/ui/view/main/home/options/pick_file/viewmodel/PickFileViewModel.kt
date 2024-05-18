package com.natiqhaciyef.prodocument.ui.view.main.home.options.pick_file.viewmodel

import com.natiqhaciyef.core.base.ui.BaseViewModel
import com.natiqhaciyef.domain.usecase.material.compress.CompressMaterialUseCase
import com.natiqhaciyef.domain.usecase.material.protect.ProtectMaterialUseCase
import com.natiqhaciyef.domain.usecase.material.split.SplitMaterialUseCase
import com.natiqhaciyef.prodocument.ui.view.main.home.options.pick_file.contract.PickFileContract
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PickFileViewModel @Inject constructor(
    private val protectMaterialUseCase: ProtectMaterialUseCase,
    private val compressMaterialUseCase: CompressMaterialUseCase,
    private val splitMaterialUseCase: SplitMaterialUseCase
): BaseViewModel<PickFileContract.PickFileState, PickFileContract.PickFileEvent, PickFileContract.PickFileEffect>() {


    override fun getInitialState(): PickFileContract.PickFileState =
        PickFileContract.PickFileState()
}