package com.natiqhaciyef.core.base.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.natiqhaciyef.common.R
import com.natiqhaciyef.common.constants.FOUR
import com.natiqhaciyef.common.constants.ONE
import com.natiqhaciyef.common.constants.THREE
import com.natiqhaciyef.common.constants.TWO
import com.natiqhaciyef.core.model.CategoryItem
import com.natiqhaciyef.core.model.FileTypes.JPEG
import com.natiqhaciyef.core.model.FileTypes.PDF
import com.natiqhaciyef.core.model.FileTypes.PNG
import com.natiqhaciyef.core.model.FileTypes.URL

open class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    protected var _binding: VB? = null
    val binding: VB
        get() = _binding!!

    fun getShareOptionsList(context: Context) = listOf(
        CategoryItem(
            id = ONE,
            title = context.getString(R.string.share_link),
            iconId = R.drawable.link_icon,
            size = null,
            type = URL,
            sizeType = null
        ),
        CategoryItem(
            id = TWO,
            title = context.getString(R.string.share_pdf),
            iconId = R.drawable.pdf_icon,
            type = PDF,
            size = null,
            sizeType = null
        ),
        CategoryItem(
            id = THREE,
            title = context.getString(R.string.share_jpg),
            iconId = R.drawable.image_icon,
            type = JPEG,
            size = null,
            sizeType = null
        ),
        CategoryItem(
            id = FOUR,
            title = context.getString(R.string.share_png),
            iconId = R.drawable.image_icon,
            type = PNG,
            size = null,
            sizeType = null
        )
    )
}
