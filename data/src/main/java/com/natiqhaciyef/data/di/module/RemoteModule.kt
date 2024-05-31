package com.natiqhaciyef.data.di.module

import android.content.Context
import android.media.session.MediaSession.Token
import com.natiqhaciyef.data.network.NetworkConfig
import com.natiqhaciyef.data.network.TokenAuthenticator
import com.natiqhaciyef.data.network.manager.TokenManager
import com.natiqhaciyef.data.network.service.MaterialService
import com.natiqhaciyef.data.network.service.PaymentService
import com.natiqhaciyef.data.network.service.QrCodeService
import com.natiqhaciyef.data.network.service.SubscriptionService
import com.natiqhaciyef.data.network.service.TokenService
import com.natiqhaciyef.data.network.service.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Provides
    @Singleton
    fun provideNetworkConfig(
        service: TokenService,
        manager: TokenManager
    ): Retrofit = Retrofit.Builder()
        .baseUrl(NetworkConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(NetworkConfig.logger
                .authenticator(TokenAuthenticator(service, manager))
                .build())
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

    @Provides
    @Singleton
    fun provideSubscriptionService(network: Retrofit): SubscriptionService =
        network.create(SubscriptionService::class.java)

    @Provides
    @Singleton
    fun providePaymentService(network: Retrofit): PaymentService =
        network.create(PaymentService::class.java)

    @Provides
    @Singleton
    fun provideTokenService(): TokenService =
        Retrofit.Builder()
            .baseUrl(NetworkConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(NetworkConfig.logger.build())
            .build()
        .create(TokenService::class.java)

}