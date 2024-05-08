package com.natiqhaciyef.data.network.response

import android.os.Parcelable
import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.core.base.network.BaseNetworkModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class QrCodeResponse(
    var url: String?,
    override var result: CRUDResponse?
) : Parcelable, BaseNetworkModel