package com.natiqhaciyef.domain.repository.impl

import com.natiqhaciyef.common.model.FaqModel
import com.natiqhaciyef.data.network.NetworkResult
import com.natiqhaciyef.common.model.ProScanInfoModel
import com.natiqhaciyef.common.model.ProscanSectionModel
import com.natiqhaciyef.data.source.AppDataSource
import com.natiqhaciyef.domain.repository.AppRepository

class AppRepositoryImpl(val ds: AppDataSource): AppRepository {
    override suspend fun getFaqList(): NetworkResult<List<FaqModel>> =
        ds.getFaqList()

    override suspend fun getProscanDetails(): NetworkResult<ProScanInfoModel> =
        ds.getProscanDetails()

    override suspend fun getProscanSections(): NetworkResult<List<ProscanSectionModel>> =
        ds.getProscanSections()

    override suspend fun getCountries(): NetworkResult<List<String>> =
        ds.getCountries()
}