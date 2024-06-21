package com.natiqhaciyef.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.natiqhaciyef.common.constants.TWO
import com.natiqhaciyef.data.local.dao.PaymentDao
import com.natiqhaciyef.data.local.dao.UserDao
import com.natiqhaciyef.data.local.entity.PaymentEntity
import com.natiqhaciyef.data.local.entity.UserEntity

@Database(entities = [UserEntity::class, PaymentEntity::class], version = TWO)
abstract class AppDatabase : RoomDatabase(){
    abstract fun getUserDao(): UserDao

    abstract fun getPaymentDao(): PaymentDao

    companion object{
        const val APP_DATABASE_NAME = "app_database"
    }
}