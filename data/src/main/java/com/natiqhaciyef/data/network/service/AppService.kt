package com.natiqhaciyef.data.network.service

import com.natiqhaciyef.common.model.ProscanSectionModel
import com.natiqhaciyef.common.model.FaqModel
import com.natiqhaciyef.common.model.ProScanInfoModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface AppService {

    @GET("")
    suspend fun getFaqList(): Response<List<FaqModel>>

    @GET("")
    suspend fun getProscanDetails(): Response<ProScanInfoModel>

    @GET("")
    suspend fun getProscanSections(): Response<List<ProscanSectionModel>>

    @GET("")
    suspend fun getCountries(): Response<List<String>>
}