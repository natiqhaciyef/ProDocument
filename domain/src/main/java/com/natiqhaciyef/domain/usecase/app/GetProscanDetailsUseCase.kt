package com.natiqhaciyef.domain.usecase.app

import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.common.model.ProScanInfoModel
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.domain.repository.AppRepository
import com.natiqhaciyef.core.base.usecase.handleFlowResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@UseCase
class GetProscanDetailsUseCase @Inject constructor(
    appRepository: AppRepository
): BaseUseCase<AppRepository, Unit, ProScanInfoModel>(appRepository) {

    override fun invoke(): Flow<Resource<ProScanInfoModel>> {
        return handleFlowResult(
            networkResult = { repository.getProscanDetails() },
            operation = { Resource.success(it) }
        )
    }
}