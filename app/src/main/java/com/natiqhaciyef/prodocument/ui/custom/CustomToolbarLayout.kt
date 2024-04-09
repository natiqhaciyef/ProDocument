package com.natiqhaciyef.prodocument.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.core.widget.doOnTextChanged
import com.google.android.material.appbar.MaterialToolbar
import com.natiqhaciyef.prodocument.databinding.CustomToolbarLayoutBinding

class CustomToolbarLayout(
    context: Context,
    attributeSet: AttributeSet? = null,
    defineStyle: Int = 0
) : MaterialToolbar(context, attributeSet, defineStyle) {
    private var binding: CustomToolbarLayoutBinding? = null
    private var searchClick = false

    init {
        initBinding()
    }

    private fun initBinding() {
        binding = CustomToolbarLayoutBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setTitleToolbar(title: String) { binding?.topbarTitle?.text = title }

    fun setVisibilityOptionsMenu(visibility: Int) { binding?.optionsIcon?.visibility = visibility }

    fun setVisibilitySearch(visibility: Int) { binding?.searchIcon?.visibility = visibility }

    fun listenSearchText(action: (CharSequence?, Int, Int, Int) -> Unit) { binding?.topbarSearch?.doOnTextChanged(action) }

    fun searchSetOnClickListener() {
        searchClick = !searchClick
        binding?.apply {
            if (searchClick) {
                topbarSearch.visibility = View.VISIBLE
                topbarTitle.visibility = View.GONE
                topbarImage.visibility = View.GONE
            } else {
                topbarImage.visibility = View.VISIBLE
                topbarTitle.visibility = View.VISIBLE
                topbarSearch.visibility = View.GONE
            }
        }
    }

    fun optionSetOnClickListener(onClickAction: () -> Unit) {
        binding?.optionsIcon?.setOnClickListener {
            onClickAction.invoke()
        }
    }


//    if (searchIconClick) {
//        topbarImage.visibility = View.GONE
//        topbarTitle.visibility = View.GONE
//        topbarSearch.visibility = View.VISIBLE
//    } else {
//        topbarImage.visibility = View.VISIBLE
//        topbarTitle.visibility = View.VISIBLE
//        topbarSearch.visibility = View.GONE
//    }
}