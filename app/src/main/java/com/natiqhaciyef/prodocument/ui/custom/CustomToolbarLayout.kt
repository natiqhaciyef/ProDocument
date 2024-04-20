package com.natiqhaciyef.prodocument.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import com.google.android.material.appbar.MaterialToolbar
import com.natiqhaciyef.prodocument.databinding.CustomToolbarLayoutBinding

class CustomToolbarLayout(
    private val context: Context,
    attributeSet: AttributeSet? = null,
) : MaterialToolbar(context, attributeSet) {
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
                toolbarImage.visibility = View.GONE
            } else {
                toolbarImage.visibility = View.VISIBLE
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

    fun changeVisibility(visibility: Int){
        binding?.toolbarImage?.visibility = visibility
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