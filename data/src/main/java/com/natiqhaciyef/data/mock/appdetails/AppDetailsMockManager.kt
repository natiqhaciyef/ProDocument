package com.natiqhaciyef.data.mock.appdetails

import com.natiqhaciyef.common.model.FaqModel
import com.natiqhaciyef.common.model.ProScanInfoModel

object AppDetailsMockManager {

    fun getFaqList() = listOf(
        FaqModel(
            title = "What is Proscan ?",
            description = "Proscan is a modern utilized, multiple functionality container for document based actions. Proscan has huge community support and almost free...",
            category = "General"
        ),
        FaqModel(
            title = "Is the Proscan app free ?",
            description = "Not fully but most parts of app is free. Furthermore, if you need to work on documents professionally, there are a few options as a subscription plan",
            category = "General"
        )
    )

    fun getProscanDetails() = ProScanInfoModel(
        title = "Proscan",
        description = "App were developed by Natiq Hajiyev.",
        icon = "",
        version = "v1.0.1"
    )

}