package com.natiqhaciyef.common.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.natiqhaciyef.common.R

enum class ContactMethods(
    @StringRes val title: Int,
    @DrawableRes val icon: Int
) {
    CONTACT_OVER_APP(R.string.contact_us, R.drawable.headphone_icon),
    WHATSAPP(R.string.whatsapp, R.drawable.whatsapp_icon),
    INSTAGRAM(R.string.instagram, R.drawable.instagram_icon),
    FACEBOOK(R.string.facebook, R.drawable.facebook_icon),
    TWITTER(R.string.twitter, R.drawable.twitter_icon),
    WEB(R.string.website, R.drawable.world_icon);

    companion object {
        fun makeContactList() =
            listOf(CONTACT_OVER_APP, WHATSAPP, INSTAGRAM, FACEBOOK, TWITTER, WEB)
    }
}