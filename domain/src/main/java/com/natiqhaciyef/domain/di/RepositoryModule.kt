package com.natiqhaciyef.domain.di

import com.natiqhaciyef.data.source.MaterialDataSource
import com.natiqhaciyef.data.source.PaymentDataSource
import com.natiqhaciyef.data.source.QrCodeDataSource
import com.natiqhaciyef.data.source.SubscriptionDataSource
import com.natiqhaciyef.data.source.UserDataSource
import com.natiqhaciyef.domain.repository.MaterialRepository
import com.natiqhaciyef.domain.repository.PaymentRepository
import com.natiqhaciyef.domain.repository.QrCodeRepository
import com.natiqhaciyef.domain.repository.SubscriptionRepository
import com.natiqhaciyef.domain.repository.UserRepository
import com.natiqhaciyef.domain.repository.impl.MaterialRepositoryImpl
import com.natiqhaciyef.domain.repository.impl.PaymentRepositoryImpl
import com.natiqhaciyef.domain.repository.impl.QrCodeRepositoryImpl
import com.natiqhaciyef.domain.repository.impl.SubscriptionRepositoryImpl
import com.natiqhaciyef.domain.repository.impl.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(ds: UserDataSource) =
        UserRepositoryImpl(ds) as UserRepository

    @Provides
    @Singleton
    fun provideMaterialRepository(ds: MaterialDataSource) =
        MaterialRepositoryImpl(ds) as MaterialRepository

    @Provides
    @Singleton
    fun provideQrCodeRepository(ds: QrCodeDataSource) =
        QrCodeRepositoryImpl(ds) as QrCodeRepository

    @Provides
    @Singleton
    fun provideSubscriptionRepository(ds: SubscriptionDataSource) =
        SubscriptionRepositoryImpl(ds) as SubscriptionRepository

    @Provides
    @Singleton
    fun providePaymentRepository(ds: PaymentDataSource) =
        PaymentRepositoryImpl(ds) as PaymentRepository

}