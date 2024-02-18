package com.natiqhaciyef.prodocument.data.base

import android.os.Parcelable
import com.natiqhaciyef.prodocument.data.network.response.CRUDResponse


interface BaseNetworkModel: Parcelable {
    var resultCode: CRUDResponse
}