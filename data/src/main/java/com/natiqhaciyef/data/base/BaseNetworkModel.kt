package com.natiqhaciyef.data.base

import android.os.Parcelable
import com.natiqhaciyef.data.network.response.CRUDResponse


interface BaseNetworkModel: Parcelable {
    var result: CRUDResponse?
}