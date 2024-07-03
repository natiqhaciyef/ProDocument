package com.natiqhaciyef.data.di

import com.natiqhaciyef.data.repository.AppRepositoryImpl
import com.natiqhaciyef.data.repository.MaterialRepositoryImpl
import com.natiqhaciyef.data.repository.PaymentRepositoryImpl
import com.natiqhaciyef.data.repository.SubscriptionRepositoryImpl
import com.natiqhaciyef.data.repository.UserRepositoryImpl
import com.natiqhaciyef.domain.repository.AppRepository
import com.natiqhaciyef.domain.repository.MaterialRepository
import com.natiqhaciyef.domain.repository.PaymentRepository
import com.natiqhaciyef.domain.repository.SubscriptionRepository
import com.natiqhaciyef.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun provideAppRepository(appRepositoryImpl: AppRepositoryImpl): AppRepository

    @Binds
    fun provideMaterialRepository(materialRepositoryImpl: MaterialRepositoryImpl): MaterialRepository

    @Binds
    fun providePaymentRepository(paymentRepositoryImpl: PaymentRepositoryImpl): PaymentRepository

    @Binds
    fun provideSubscriptionRepository(subscriptionRepositoryImpl: SubscriptionRepositoryImpl): SubscriptionRepository

    @Binds
    fun provideUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository
}