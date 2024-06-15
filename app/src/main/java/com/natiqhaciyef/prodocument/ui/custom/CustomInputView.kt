package com.natiqhaciyef.prodocument.ui.custom

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.natiqhaciyef.common.R
import androidx.core.widget.doOnTextChanged
import com.natiqhaciyef.prodocument.databinding.CustomInputViewBinding

class CustomInputView(
    private val context: Context,
    private val attributeSet: AttributeSet
) : ConstraintLayout(context, attributeSet) {
    private var binding: CustomInputViewBinding? = null

    init {
        binding = CustomInputViewBinding.inflate(LayoutInflater.from(context), this, true)
        defaultInit()
    }

    fun initInput(title: String, inputHint: String) {
        binding?.let {
            it.customTitleText.text = title
            it.customInputEditText.hint = inputHint
        }
    }

    fun listenUserInput(action: (CharSequence?, Int, Int, Int) -> Unit) {
        binding?.customInputEditText?.doOnTextChanged(action)
    }

    fun getInputResult(): String {
        return binding?.customInputEditText?.text?.toString() ?: ""
    }

    private fun getCustomInputType(inpType: String) = when (inpType) {
        "text" -> InputType.TYPE_TEXT_VARIATION_NORMAL
        "email" -> InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        "password" -> InputType.TYPE_TEXT_VARIATION_PASSWORD
        "number" -> InputType.TYPE_NUMBER_VARIATION_NORMAL
        "decimal" -> InputType.TYPE_NUMBER_FLAG_DECIMAL
        else -> InputType.TYPE_TEXT_VARIATION_NORMAL
    }

    private fun defaultInit() {
        val typedArray = context.obtainStyledAttributes(
            attributeSet,
            R.styleable.com_natiqhaciyef_prodocument_ui_custom_CustomInputView
        )
        val customHint =
            typedArray.getString(R.styleable.com_natiqhaciyef_prodocument_ui_custom_CustomInputView_customHint)
        val customTitle =
            typedArray.getString(R.styleable.com_natiqhaciyef_prodocument_ui_custom_CustomInputView_customTitle)
        val customInputType =
            typedArray.getString(R.styleable.com_natiqhaciyef_prodocument_ui_custom_CustomInputView_customInputType)
        binding?.customInputEditText?.hint = customHint
        binding?.customTitleText?.text = customTitle
        customInputType?.let {
            binding?.customInputEditText?.setRawInputType(getCustomInputType(it))
        }
    }
}