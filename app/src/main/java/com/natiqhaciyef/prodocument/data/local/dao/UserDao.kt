package com.natiqhaciyef.prodocument.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.natiqhaciyef.prodocument.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM user_table")
    suspend fun getAllUser(): List<UserEntity>?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(userEntity: UserEntity)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateUser(userEntity: UserEntity)

    @Delete
    suspend fun removeUser(userEntity: UserEntity)
}