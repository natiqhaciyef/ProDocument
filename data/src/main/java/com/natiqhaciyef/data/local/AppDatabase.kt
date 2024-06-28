package com.natiqhaciyef.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.natiqhaciyef.common.constants.THREE
import com.natiqhaciyef.data.local.dao.PaymentDao
import com.natiqhaciyef.data.local.entity.PaymentEntity

@Database(entities = [PaymentEntity::class], version = THREE)
abstract class AppDatabase : RoomDatabase(){

    abstract fun getPaymentDao(): PaymentDao

    companion object{
        const val APP_DATABASE_NAME = "app_database"
    }
}