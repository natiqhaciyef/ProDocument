package com.natiqhaciyef.domain.usecase.app

import com.natiqhaciyef.common.model.FaqModel
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.domain.repository.AppRepository
import com.natiqhaciyef.core.base.usecase.handleFlowResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@UseCase
class GetFaqListUseCase @Inject constructor(
    appRepository: AppRepository
): BaseUseCase<AppRepository, Unit, List<FaqModel>>(appRepository) {

    override fun invoke(): Flow<Resource<List<FaqModel>>>  {

        return handleFlowResult(
            networkResult = { repository.getFaqList() },
            operation = { Resource.success(it) }
        )
    }
}