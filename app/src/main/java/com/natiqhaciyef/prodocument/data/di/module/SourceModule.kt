package com.natiqhaciyef.prodocument.data.di.module

import com.natiqhaciyef.prodocument.data.local.dao.UserDao
import com.natiqhaciyef.prodocument.data.network.service.MaterialService
import com.natiqhaciyef.prodocument.data.network.service.UserService
import com.natiqhaciyef.prodocument.data.source.MaterialDataSource
import com.natiqhaciyef.prodocument.data.source.UserDataSource
import com.natiqhaciyef.prodocument.domain.repository.MaterialRepository
import com.natiqhaciyef.prodocument.domain.repository.UserRepository
import com.natiqhaciyef.prodocument.domain.repository.impl.MaterialRepositoryImpl
import com.natiqhaciyef.prodocument.domain.repository.impl.UserRepositoryImpl
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