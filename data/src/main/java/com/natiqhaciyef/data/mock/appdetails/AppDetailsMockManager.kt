package com.natiqhaciyef.data.mock.appdetails

import com.natiqhaciyef.common.model.ProscanSectionModel
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
        ),
        FaqModel(
            title = "Is E-sign only for premium users ?",
            description = "E-sign is fully free feature for users. You can use it just accepting terms & conditions",
            category = "Files & Process"
        )
    )

    fun getProscanDetails() = ProScanInfoModel(
        title = "Proscan",
        description = "App were developed by Natiq Hajiyev.",
        icon = "",
        version = "1.0.1"
    )

    fun getProscanSections() = listOf(
        ProscanSectionModel(title = "Job Vacancies", link = "https://www.hellojob.az/"),
        ProscanSectionModel(title = "Teams", link = "https://github.com/natiqhaciyef/"),
        ProscanSectionModel(title = "Partners", link = "https://www.udemy.com/home/my-courses/learning/"),
        ProscanSectionModel(title = "Accessibility", link = "https://stevdza-san.com/"),
        ProscanSectionModel(title = "Privacy Policy", link = "https://play.google.com/console/u/0/developers/8825372601260754680/app-list?pli=1"),
        ProscanSectionModel(title = "Feedback", link = "https://medium.com/"),
//        ProscanSectionModel(title = "Rate us", link = "https://github.com/natiqhaciyef/"),
    )
}