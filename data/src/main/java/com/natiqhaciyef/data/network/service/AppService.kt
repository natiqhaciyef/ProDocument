package com.natiqhaciyef.data.network.service

import com.natiqhaciyef.common.model.ProscanSectionModel
import com.natiqhaciyef.common.model.FaqModel
import com.natiqhaciyef.common.model.ProScanInfoModel
import retrofit2.Response
import retrofit2.http.GET

interface AppService {

    @GET("")
    suspend fun getFaqList(): Response<List<FaqModel>>

    @GET("")
    suspend fun getProscanDetails(): Response<ProScanInfoModel>

    @GET("")
    suspend fun getProscanSections(): Response<List<ProscanSectionModel>>

    @GET("https://raw.githubusercontent.com/natiqhaciyef/Country-JSON/main/Country%20API%20kit/rawCountryNames.json?token=GHSAT0AAAAAACMK5HIY76CYKFNJ3WR3VPU4ZT5SAQQ")
    suspend fun getCountries(): Response<List<String>>
}