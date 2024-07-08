package com.natiqhaciyef.data.source

import com.natiqhaciyef.core.base.mock.generateMockerClass
import com.natiqhaciyef.data.mock.appdetails.GetFaqListMockGenerator
import com.natiqhaciyef.data.mock.appdetails.GetProscanDetailsMockGenerator
import com.natiqhaciyef.data.mock.appdetails.GetProscanSectionsMockGenerator
import com.natiqhaciyef.core.base.network.LoadType
import com.natiqhaciyef.core.base.network.handleNetworkResponse
import com.natiqhaciyef.data.network.service.AppService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AppDataSource @Inject constructor(val service: AppService) {

    suspend fun getFaqList() = withContext(Dispatchers.IO) {
        val mock = generateMockerClass(GetFaqListMockGenerator::class, Unit)
            .getMock(Unit) { null }
        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.getFaqList()
        }
    }

    suspend fun getProscanDetails() = withContext(Dispatchers.IO) {
        val mock = generateMockerClass(GetProscanDetailsMockGenerator::class, Unit)
            .getMock(Unit) { null }
        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.getProscanDetails()
        }
    }

    suspend fun getProscanSections() = withContext(Dispatchers.IO) {
        val mock = generateMockerClass(GetProscanSectionsMockGenerator::class, Unit)
            .getMock(Unit) { null }
        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.getProscanSections()
        }
    }

    suspend fun getCountries() = withContext(Dispatchers.IO) {
        handleNetworkResponse(mock = null, handlingType = LoadType.MOCK) {
            service.getCountries()
        }
    }
}