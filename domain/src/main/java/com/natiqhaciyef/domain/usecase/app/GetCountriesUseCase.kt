package com.natiqhaciyef.domain.usecase.app

import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.core.base.usecase.handleFlowResult
import com.natiqhaciyef.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@UseCase
class GetCountriesUseCase @Inject constructor(
    appRepository: AppRepository
) : BaseUseCase<AppRepository, Unit, List<String>>(appRepository) {

    override fun invoke(): Flow<Resource<List<String>>> {
        return handleFlowResult(
            networkResult = { repository.getCountries() },
            operation = { Resource.success(it) }
        )
    }

}