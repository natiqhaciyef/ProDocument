package com.natiqhaciyef.common.model

import androidx.annotation.DrawableRes
import com.natiqhaciyef.common.R

// tell backend sends graph category names as enum.name
enum class GraphCategory(@DrawableRes val icon: Int) {
    SCAN(R.drawable.scan_code_menu_icon),
    WATERMARK(R.drawable.watermark_menu_icon),
    E_SIGN(R.drawable.e_sign_menu_icon),
    SPLIT(R.drawable.split_pdf_menu_icon),
    MERGE(R.drawable.merge_pdf_menu_icon),
    PROTECT(R.drawable.protect_pdf_menu_icon),
    COMPRESS(R.drawable.compress_pdf_menu_icon);

    companion object {
        fun stringToIcon(title: String) = when (title.lowercase()) {
            SCAN.name.lowercase() -> SCAN.icon
            WATERMARK.name.lowercase() -> WATERMARK.icon
            E_SIGN.name.lowercase() -> E_SIGN.icon
            SPLIT.name.lowercase() -> SPLIT.icon
            MERGE.name.lowercase() -> MERGE.icon
            PROTECT.name.lowercase() -> PROTECT.icon
            COMPRESS.name.lowercase() -> COMPRESS.icon
            else -> SCAN.icon
        }

        fun iconToString(@DrawableRes icon: Int) = when(icon){
            R.drawable.scan_code_menu_icon -> SCAN.name
            R.drawable.watermark_menu_icon -> WATERMARK.name
            R.drawable.e_sign_menu_icon -> E_SIGN.name
            R.drawable.split_pdf_menu_icon -> SPLIT.name
            R.drawable.merge_pdf_menu_icon -> MERGE.name
            R.drawable.protect_pdf_menu_icon -> PROTECT.name
            R.drawable.compress_pdf_menu_icon -> COMPRESS.name
            else -> SCAN.name
        }
    }
}