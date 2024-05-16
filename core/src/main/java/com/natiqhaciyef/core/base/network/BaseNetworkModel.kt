package com.natiqhaciyef.core.base.network

import android.os.Parcelable
import com.natiqhaciyef.core.CRUDResponse

interface BaseNetworkModel: Parcelable {
    var result: CRUDResponse?
}