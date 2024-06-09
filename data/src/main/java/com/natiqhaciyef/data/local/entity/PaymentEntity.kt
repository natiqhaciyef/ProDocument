package com.natiqhaciyef.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.natiqhaciyef.common.model.payment.PaymentDetails

@Entity("payment_entity")
data class PaymentEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var merchantId: Int,
    var paymentType: String,
    var paymentMethod: String,
    var paymentDetails: String,
)