package com.natiqhaciyef.prodocument.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.databinding.CustomExpandedDropdownViewBinding

class CustomExpandedDropDownCardView(
    private val ctx: Context,
    private var attributes: AttributeSet
) : ConstraintLayout(ctx, attributes) {
    private var binding: CustomExpandedDropdownViewBinding? = null
    private var genderSelection: String = "Not-selected"

    init {
        binding = CustomExpandedDropdownViewBinding.inflate(LayoutInflater.from(ctx), this, true)
        defaultInit()
    }

    fun initCustomDropDown(list: List<String>, title: String, hint: String) {
        val adapter =
            ArrayAdapter(ctx, R.layout.drop_down_gender_item, list)

        binding?.let {
            it.dropdownInputItem.hint = hint
            it.dropdownInputItem.setAdapter(adapter)
            it.dropdownInputItem.setOnItemClickListener { adapterView, _, p, _ ->
                list.forEach { item ->
                    if (adapterView.getItemAtPosition(p).toString() == item)
                        genderSelection = item
                }
            }

            it.dropdownTitle.text = title
        }
    }

    fun initPickedData(
        input: String,
        title: String? = null
    ) {
        if (title != null)
            binding?.dropdownTitle?.text = title
        binding?.dropdownInputItem?.setText(input)
    }

    private fun defaultInit() {
        val defaultList = listOf("Male", "Female")
        val typedArray = context.obtainStyledAttributes(
            attributes,
            com.natiqhaciyef.common.R.styleable.com_natiqhaciyef_prodocument_ui_custom_CustomExpandedDropDownCardView
        )
        val customHint =
            typedArray.getString(com.natiqhaciyef.common.R.styleable.com_natiqhaciyef_prodocument_ui_custom_CustomExpandedDropDownCardView_customDropDownHint)
        val customTitle =
            typedArray.getString(com.natiqhaciyef.common.R.styleable.com_natiqhaciyef_prodocument_ui_custom_CustomExpandedDropDownCardView_customDropDownTitle)

        val adapter =
            ArrayAdapter(ctx, R.layout.drop_down_gender_item, defaultList)
        binding?.let {
            it.dropdownTitle.text = customTitle
            it.dropdownInputItem.hint = customHint
            it.dropdownInputItem.setAdapter(adapter)
            it.dropdownInputItem.setOnItemClickListener { adapterView, _, p, _ ->
                defaultList.forEach { item ->
                    if (adapterView.getItemAtPosition(p).toString() == item)
                        genderSelection = item
                }
            }

        }
    }

    fun getPickedItem(): String = genderSelection
}