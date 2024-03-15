package com.natiqhaciyef.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.natiqhaciyef.data.local.dao.UserDao
import com.natiqhaciyef.data.local.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun getUserDao(): UserDao
}