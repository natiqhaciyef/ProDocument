package com.natiqhaciyef.prodocument.ui.util

import android.content.Context
import com.natiqhaciyef.common.R
import com.natiqhaciyef.common.model.MenuItemModel
import com.natiqhaciyef.prodocument.ui.util.BaseNavigationDeepLink.ALL_TOOLS_ROUTE
import com.natiqhaciyef.prodocument.ui.util.BaseNavigationDeepLink.COMPRESS_ROUTE
import com.natiqhaciyef.prodocument.ui.util.BaseNavigationDeepLink.COMPRESS_TYPE
import com.natiqhaciyef.prodocument.ui.util.BaseNavigationDeepLink.E_SIGN_ROUTE
import com.natiqhaciyef.prodocument.ui.util.BaseNavigationDeepLink.MERGE_ROUTE
import com.natiqhaciyef.prodocument.ui.util.BaseNavigationDeepLink.PROTECT_ROUTE
import com.natiqhaciyef.prodocument.ui.util.BaseNavigationDeepLink.PROTECT_TYPE
import com.natiqhaciyef.prodocument.ui.util.BaseNavigationDeepLink.SCAN_ROUTE
import com.natiqhaciyef.prodocument.ui.util.BaseNavigationDeepLink.SPLIT_ROUTE
import com.natiqhaciyef.prodocument.ui.util.BaseNavigationDeepLink.SPLIT_TYPE
import com.natiqhaciyef.prodocument.ui.util.BaseNavigationDeepLink.WATERMARK_ROUTE


object UiList {
    fun generateHomeMenuItemsList(context: Context) = listOf(
        MenuItemModel(
            title = context.getString(R.string.scan_code),
            imageId = R.drawable.scan_code_menu_icon,
            routeTitle = SCAN_ROUTE
        ),
        MenuItemModel(
            title = context.getString(R.string.watermark),
            imageId = R.drawable.watermark_menu_icon,
            routeTitle = WATERMARK_ROUTE
        ),
        MenuItemModel(
            title = context.getString(R.string.e_sign_pdf),
            imageId = R.drawable.e_sign_menu_icon,
            routeTitle = E_SIGN_ROUTE
        ),
        MenuItemModel(
            title = context.getString(R.string.split_pdf),
            imageId = R.drawable.split_pdf_menu_icon,
            routeTitle = SPLIT_ROUTE,
            type = SPLIT_TYPE
        ),
        MenuItemModel(
            title = context.getString(R.string.merge_pdf),
            imageId = R.drawable.merge_pdf_menu_icon,
            routeTitle = MERGE_ROUTE
        ),
        MenuItemModel(
            title = context.getString(R.string.protect_pdf),
            imageId = R.drawable.protect_pdf_menu_icon,
            routeTitle = PROTECT_ROUTE,
            type = PROTECT_TYPE
        ),
        MenuItemModel(
            title = context.getString(R.string.compress_pdf),
            imageId = R.drawable.compress_pdf_menu_icon,
            routeTitle = COMPRESS_ROUTE,
            type = COMPRESS_TYPE
        ),
        MenuItemModel(
            title = context.getString(R.string.all_tools),
            imageId = R.drawable.all_tools_menu_icon,
            routeTitle = ALL_TOOLS_ROUTE
        )
    )
}