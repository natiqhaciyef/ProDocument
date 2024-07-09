package com.natiqhaciyef.domain.usecase.app

import com.natiqhaciyef.common.model.ProscanSectionModel
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.domain.repository.AppRepository
import com.natiqhaciyef.core.base.usecase.handleFlowResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@UseCase
class GetProscanSectionsUseCase @Inject constructor(
    appRepository: AppRepository
): BaseUseCase<AppRepository, Unit, List<ProscanSectionModel>>(appRepository) {

    override fun invoke(): Flow<Resource<List<ProscanSectionModel>>> {
        return handleFlowResult(
            networkResult = { repository.getProscanSections() },
            operation = { Resource.success(it) }
        )
    }

}