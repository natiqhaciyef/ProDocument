package com.natiqhaciyef.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.natiqhaciyef.common.model.payment.PaymentDetails
import com.natiqhaciyef.data.local.entity.PaymentEntity

@Dao
interface PaymentDao{

    @Query("SELECT * FROM payment_entity")
    suspend fun getStoredPaymentMethods(): List<PaymentEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNewPaymentMethod(paymentEntity: PaymentEntity)

    @Query("DELETE FROM payment_entity WHERE paymentDetails = :detail")
    suspend fun removePaymentMethod(detail: String)
}
