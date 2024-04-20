package com.natiqhaciyef.data.di.module

import com.natiqhaciyef.data.network.NetworkConfig
import com.natiqhaciyef.data.network.service.MaterialService
import com.natiqhaciyef.data.network.service.QrCodeService
import com.natiqhaciyef.data.network.service.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Provides
    @Singleton
    fun provideNetworkConfig(): Retrofit = Retrofit.Builder()
        .baseUrl(NetworkConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(NetworkConfig.logger.build())
        .build()

    @Provides
    @Singleton
    fun provideUserService(network: Retrofit): UserService =
        network.create(UserService::class.java)

    @Provides
    @Singleton
    fun provideMaterialService(network: Retrofit): MaterialService =
        network.create(MaterialService::class.java)

    @Provides
    @Singleton
    fun provideQrCodeService(network: Retrofit): QrCodeService =
        network.create(QrCodeService::class.java)

}