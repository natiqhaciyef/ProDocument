package com.natiqhaciyef.prodocument.ui.base

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.natiqhaciyef.common.R
import com.natiqhaciyef.domain.worker.config.JPEG
import com.natiqhaciyef.domain.worker.config.PDF
import com.natiqhaciyef.domain.worker.config.PNG
import com.natiqhaciyef.domain.worker.config.URL
import com.natiqhaciyef.prodocument.ui.model.CategoryItem

open class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    protected var _binding: VB? = null
    val binding: VB
        get() = _binding!!

    fun getShareOptionsList(context: Context) = listOf(
        CategoryItem(
            id = 1,
            title = context.getString(R.string.share_link),
            iconId = R.drawable.link_icon,
            size = null,
            type = URL,
            sizeType = null
        ),
        CategoryItem(
            id = 2,
            title = context.getString(R.string.share_pdf),
            iconId = R.drawable.pdf_icon,
            type = PDF,
            size = null,
            sizeType = null
        ),
        CategoryItem(
            id = 3,
            title = context.getString(R.string.share_jpg),
            iconId = R.drawable.image_icon,
            type = JPEG,
            size = null,
            sizeType = null
        ),
        CategoryItem(
            id = 4,
            title = context.getString(R.string.share_png),
            iconId = R.drawable.image_icon,
            type = PNG,
            size = null,
            sizeType = null
        )
    )
}