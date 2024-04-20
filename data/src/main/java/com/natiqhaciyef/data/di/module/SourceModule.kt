package com.natiqhaciyef.data.di.module

import com.natiqhaciyef.data.local.dao.UserDao
import com.natiqhaciyef.data.network.service.MaterialService
import com.natiqhaciyef.data.network.service.QrCodeService
import com.natiqhaciyef.data.network.service.UserService
import com.natiqhaciyef.data.source.MaterialDataSource
import com.natiqhaciyef.data.source.QrCodeDataSource
import com.natiqhaciyef.data.source.UserDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SourceModule {

    @Provides
    @Singleton
    fun provideUserDataSource(service: UserService, dao: UserDao) =
        UserDataSource(service, dao)

    @Provides
    @Singleton
    fun provideMaterialDataSource(service: MaterialService) =
        MaterialDataSource(service)

    @Provides
    @Singleton
    fun provideQrCodeDataSource(service: QrCodeService) =
        QrCodeDataSource(service)

}