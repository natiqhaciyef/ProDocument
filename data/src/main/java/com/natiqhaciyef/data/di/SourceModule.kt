package com.natiqhaciyef.data.di

import android.content.Context
import com.natiqhaciyef.data.network.manager.TokenManager
import com.natiqhaciyef.data.network.service.AppService
import com.natiqhaciyef.data.network.service.MaterialService
import com.natiqhaciyef.data.network.service.PaymentService
import com.natiqhaciyef.data.network.service.SubscriptionService
import com.natiqhaciyef.data.network.service.UserService
import com.natiqhaciyef.data.source.AppDataSource
import com.natiqhaciyef.data.source.MaterialDataSource
import com.natiqhaciyef.data.source.PaymentDataSource
import com.natiqhaciyef.data.source.SubscriptionDataSource
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
    fun provideUserDataSource(manager: TokenManager, service: UserService) =
        UserDataSource(manager, service)

    @Provides
    @Singleton
    fun provideMaterialDataSource(manager: TokenManager, service: MaterialService) =
        MaterialDataSource(manager, service)

    @Provides
    @Singleton
    fun provideSubscriptionDataSource(manager: TokenManager, service: SubscriptionService) =
        SubscriptionDataSource(manager, service)

    @Provides
    @Singleton
    fun providePaymentDataSource(manager: TokenManager, service: PaymentService) =
        PaymentDataSource(manager, service)

    @Provides
    @Singleton
    fun provideAppDataSource(service: AppService) =
        AppDataSource(service)
}