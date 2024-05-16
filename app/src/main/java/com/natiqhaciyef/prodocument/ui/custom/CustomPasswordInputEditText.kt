package com.natiqhaciyef.prodocument.ui.custom

import android.content.Context
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import com.natiqhaciyef.common.R
import com.natiqhaciyef.prodocument.databinding.CustomPasswordInputEdittextBinding

class CustomPasswordInputEditText(
    private val context: Context,
    attribute: AttributeSet
) : ConstraintLayout(context, attribute) {
    private var binding: CustomPasswordInputEdittextBinding? = null
    private var isPasswordVisible: Boolean = false
    val text = getPasswordText()

    init {
        initBinding()
    }

    private fun initBinding() {
        binding =
            CustomPasswordInputEdittextBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun getPasswordText(): String =
        (binding?.passwordInput?.text != null).toString()


    fun setPasswordHintText(text: String) = binding?.passwordInput?.setHint(text)

    fun setPasswordHintText(textId: Int) = binding?.passwordInput?.setHint(context.getString(textId))

    fun setPasswordText(text: String) = binding?.passwordInput?.setText(text)

    fun setPasswordText(textId: Int) =
        binding?.passwordInput?.setText(context.getString(textId))

    fun setPasswordTitleText(textId: Int) {
        binding?.passwordTitle?.setText(context.getString(textId))
    }

    fun setPasswordTitleText(text: String) {
        binding?.passwordTitle?.setText(text)
    }

    fun changeVisibility() {
        binding?.apply {
            passwordVisibilityIcon.setOnClickListener {
                isPasswordVisible = !isPasswordVisible
                if (isPasswordVisible) {
                    passwordInput.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    passwordVisibilityIcon.setImageResource(R.drawable.visibility_on_icon)
                } else {
                    passwordInput.transformationMethod =
                        PasswordTransformationMethod.getInstance()
                    passwordVisibilityIcon.setImageResource(R.drawable.visibility_off_icon)
                }
            }
        }
    }

    fun customDoOnTextChangeListener(listener: (CharSequence?, Int, Int, Int) -> Unit) {
        binding?.apply {
            passwordInput.doOnTextChanged(listener)
        }
    }
}