package com.natiqhaciyef.domain.repository

import com.natiqhaciyef.common.model.FaqModel
import com.natiqhaciyef.core.base.repository.BaseRepository
import com.natiqhaciyef.common.model.ProScanInfoModel
import com.natiqhaciyef.common.model.ProscanSectionModel
import com.natiqhaciyef.core.base.network.NetworkResult

interface AppRepository: BaseRepository {

    suspend fun getFaqList(): NetworkResult<List<FaqModel>>

    suspend fun getProscanDetails(): NetworkResult<ProScanInfoModel>

    suspend fun getProscanSections(): NetworkResult<List<ProscanSectionModel>>

    suspend fun getCountries(): NetworkResult<List<String>>
}