package com.natiqhaciyef.data.di.module

import android.content.Context
import com.natiqhaciyef.data.local.dao.UserDao
import com.natiqhaciyef.data.network.manager.TokenManager
import com.natiqhaciyef.data.network.service.MaterialService
import com.natiqhaciyef.data.network.service.QrCodeService
import com.natiqhaciyef.data.network.service.UserService
import com.natiqhaciyef.data.source.MaterialDataSource
import com.natiqhaciyef.data.source.QrCodeDataSource
import com.natiqhaciyef.data.source.UserDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SourceModule {

    @Provides
    @Singleton
    fun provideTokenManager(@ApplicationContext context: Context) =
        TokenManager(context)

    @Provides
    @Singleton
    fun provideUserDataSource(service: UserService, dao: UserDao) =
        UserDataSource(service, dao)

    @Provides
    @Singleton
    fun provideMaterialDataSource(manager: TokenManager, service: MaterialService) =
        MaterialDataSource(manager, service)

    @Provides
    @Singleton
    fun provideQrCodeDataSource(service: QrCodeService) =
        QrCodeDataSource(service)

}