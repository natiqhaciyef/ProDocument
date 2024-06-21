package com.natiqhaciyef.data.di.module

import android.content.Context
import androidx.room.Room
import com.natiqhaciyef.data.local.AppDatabase
import com.natiqhaciyef.data.local.AppDatabase.Companion.APP_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, AppDatabase::class.java, APP_DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideUserDao(db: AppDatabase) = db.getUserDao()

    @Provides
    @Singleton
    fun providePaymentDao(db: AppDatabase) = db.getPaymentDao()
}