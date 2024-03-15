package com.natiqhaciyef.domain.di

import com.natiqhaciyef.data.local.dao.UserDao
import com.natiqhaciyef.data.network.service.MaterialService
import com.natiqhaciyef.data.network.service.UserService
import com.natiqhaciyef.data.source.MaterialDataSource
import com.natiqhaciyef.data.source.UserDataSource
import com.natiqhaciyef.domain.repository.MaterialRepository
import com.natiqhaciyef.domain.repository.UserRepository
import com.natiqhaciyef.domain.repository.impl.MaterialRepositoryImpl
import com.natiqhaciyef.domain.repository.impl.UserRepositoryImpl
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
    fun provideUserRepository(ds: UserDataSource) =
        UserRepositoryImpl(ds) as UserRepository

    @Provides
    @Singleton
    fun provideMaterialDataSource(service: MaterialService) =
        MaterialDataSource(service)

    @Provides
    @Singleton
    fun provideMaterialRepository(ds: MaterialDataSource) =
        MaterialRepositoryImpl(ds) as MaterialRepository


}