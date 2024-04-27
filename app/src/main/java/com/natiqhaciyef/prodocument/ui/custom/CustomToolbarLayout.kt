package com.natiqhaciyef.prodocument.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
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
        searchSetOnClickListener()
        closeAndSearchIconClickAction()
    }

    fun setTitleToolbar(title: String) {
        binding?.topbarTitle?.text = title
    }

    fun setVisibilityOptionsMenu(visibility: Int) {
        binding?.optionsIcon?.visibility = visibility
    }

    fun setVisibilitySearch(visibility: Int) {
        binding?.searchIcon?.visibility = visibility
    }

    fun listenSearchText(action: (CharSequence?, Int, Int, Int) -> Unit) {
        binding?.topbarSearch?.doOnTextChanged(action)
    }

    private fun searchSetOnClickListener() {
        binding?.apply {
            searchIcon.setOnClickListener {
                searchClick = !searchClick
                searchCLickEvent(searchClick)
            }
        }
    }

    private fun searchCLickEvent(click: Boolean) {
        binding?.apply {
            if (click) {
                topbarLinearLayout.visibility = View.GONE
                searchbarLayout.visibility = View.VISIBLE
            } else {
                topbarLinearLayout.visibility = View.VISIBLE
                searchbarLayout.visibility = View.GONE
            }
        }
    }

    fun optionSetOnClickListener(onClickAction: () -> Unit) {
        binding?.optionsIcon?.setOnClickListener {
            onClickAction.invoke()
        }
    }

    fun changeVisibility(visibility: Int) {
        binding?.toolbarImage?.visibility = visibility
        binding?.topbarTitle?.visibility = visibility
    }

    private fun closeAndSearchIconClickAction() {
        binding?.apply {
            topbarSearchLayout.setEndIconOnClickListener {
                topbarSearch.setText("")
            }
            topbarSearchLayout.setStartIconOnClickListener {
                searchClick = false
                searchCLickEvent(searchClick)
            }
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