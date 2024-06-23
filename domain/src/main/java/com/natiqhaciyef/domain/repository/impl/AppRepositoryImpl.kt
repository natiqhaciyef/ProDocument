package com.natiqhaciyef.domain.repository.impl

import com.natiqhaciyef.common.model.FaqModel
import com.natiqhaciyef.data.network.NetworkResult
import com.natiqhaciyef.common.model.ProScanInfoModel
import com.natiqhaciyef.data.source.AppDataSource
import com.natiqhaciyef.domain.repository.AppRepository

class AppRepositoryImpl(val ds: AppDataSource): AppRepository {
    override suspend fun getFaqList(): NetworkResult<List<FaqModel>> =
        ds.getFaqList()

    override suspend fun getProscanDetails(): NetworkResult<ProScanInfoModel> =
        ds.getProscanDetails()

}