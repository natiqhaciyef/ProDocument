package com.natiqhaciyef.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.natiqhaciyef.data.local.dao.PaymentDao
import com.natiqhaciyef.data.local.dao.UserDao
import com.natiqhaciyef.data.local.entity.PaymentEntity
import com.natiqhaciyef.data.local.entity.UserEntity

@Database(entities = [UserEntity::class, PaymentEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase(){
    abstract fun getUserDao(): UserDao

    abstract fun getPaymentDao(): PaymentDao
}