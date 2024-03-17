package com.natiqhaciyef.prodocument.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import com.natiqhaciyef.common.R
import com.natiqhaciyef.prodocument.databinding.CustomOtpEdittextBinding


class CustomOTPInput(
    private val context: Context,
    attribute: AttributeSet
) : ConstraintLayout(context, attribute) {
    private var binding: CustomOtpEdittextBinding? = null
    private var otpResultArray = arrayOf("0", "0", "0", "0")

    init {
        initBinding()
    }

    private fun initBinding() {
        binding =
            CustomOtpEdittextBinding.inflate(LayoutInflater.from(context), this, true)

        checkInput()
    }

    private fun checkInput() {
        binding?.apply {
            otp1.doOnTextChanged { text, start, before, count ->
                checkFocus()

                text?.let {
                    if (text.length == MAX_SINGLE_OTP_FIELD_SIZE) {
                        otp1.focusSearch(View.FOCUS_RIGHT)?.requestFocus()
                        otpResultArray[0] = text[0].toString()
                    } else if (text.length > MAX_SINGLE_OTP_FIELD_SIZE) {
                        otp1.text.clear()
                    }
                }
            }

            otp2.doOnTextChanged { text, start, before, count ->
                checkFocus()

                text?.let {
                    if (text.length == MAX_SINGLE_OTP_FIELD_SIZE) {
                        otp2.focusSearch(View.FOCUS_RIGHT)?.requestFocus()
                        otpResultArray[1] = text[0].toString()
                    } else if (text.length > MAX_SINGLE_OTP_FIELD_SIZE) {
                        otp2.text.clear()
                    }
                }
            }

            otp3.doOnTextChanged { text, start, before, count ->
                checkFocus()

                text?.let {
                    if (text.length == MAX_SINGLE_OTP_FIELD_SIZE) {
                        otp3.focusSearch(View.FOCUS_RIGHT)?.requestFocus()
                        otpResultArray[2] = text[0].toString()
                    } else if (text.length > MAX_SINGLE_OTP_FIELD_SIZE) {
                        otp3.text.clear()
                    }
                }
            }

            otp4.doOnTextChanged { text, start, before, count ->
                checkFocus()

                text?.let {
                    if (text.length == MAX_SINGLE_OTP_FIELD_SIZE) {
                        otp4.focusSearch(View.FOCUS_RIGHT)?.requestFocus()
                        otpResultArray[3] = text[0].toString()
                    } else if (text.length > MAX_SINGLE_OTP_FIELD_SIZE) {
                        otp4.text.clear()
                    }
                }
            }
        }
    }

    private fun checkFocus() {
        binding?.apply {
            if (otp1.isFocused) {
                otp1.backgroundTintList = context.getColorStateList(R.color.transparent_blue)
            } else {
                otp1.background = context.getDrawable(R.drawable.custom_otp_input)
            }

            if (otp2.isFocused) {
                otp2.backgroundTintList = context.getColorStateList(R.color.transparent_blue)
            } else {
                otp2.background = context.getDrawable(R.drawable.custom_otp_input)
            }

            if (otp3.isFocused) {
                otp3.backgroundTintList = context.getColorStateList(R.color.transparent_blue)
            } else {
                otp3.background = context.getDrawable(R.drawable.custom_otp_input)
            }

            if (otp4.isFocused) {
                otp4.backgroundTintList = context.getColorStateList(R.color.transparent_blue)
            } else {
                otp4.background = context.getDrawable(R.drawable.custom_otp_input)
            }
        }
    }

    fun getOTP(): String {
        var result = ""
        for (element in otpResultArray) {
            result += element
        }

        return if (result.length == OTP_MAX_SIZE) {
            result
        } else {
            "Element out of bound"
        }
    }

    fun buttonEnablingListener(button: AppCompatButton) {
        binding?.apply {
            otp1.doOnTextChanged { text, start, before, count ->
                button.isEnabled = text?.length == MAX_SINGLE_OTP_FIELD_SIZE
                        && otp2.text.length == MAX_SINGLE_OTP_FIELD_SIZE
                        && otp3.text.length == MAX_SINGLE_OTP_FIELD_SIZE
                        && otp4.text.length == MAX_SINGLE_OTP_FIELD_SIZE
            }

            otp2.doOnTextChanged { text, start, before, count ->
                button.isEnabled = text?.length == MAX_SINGLE_OTP_FIELD_SIZE
                        && otp1.text.length == MAX_SINGLE_OTP_FIELD_SIZE
                        && otp3.text.length == MAX_SINGLE_OTP_FIELD_SIZE
                        && otp4.text.length == MAX_SINGLE_OTP_FIELD_SIZE
            }

            otp3.doOnTextChanged { text, start, before, count ->
                button.isEnabled = text?.length == MAX_SINGLE_OTP_FIELD_SIZE
                        && otp1.text.length == MAX_SINGLE_OTP_FIELD_SIZE
                        && otp2.text.length == MAX_SINGLE_OTP_FIELD_SIZE
                        && otp4.text.length == MAX_SINGLE_OTP_FIELD_SIZE
            }

            otp4.doOnTextChanged { text, start, before, count ->
                button.isEnabled = text?.length == MAX_SINGLE_OTP_FIELD_SIZE
                        && otp1.text.length == MAX_SINGLE_OTP_FIELD_SIZE
                        && otp2.text.length == MAX_SINGLE_OTP_FIELD_SIZE
                        && otp3.text.length == MAX_SINGLE_OTP_FIELD_SIZE
            }
        }
    }

    companion object {
        private const val MAX_SINGLE_OTP_FIELD_SIZE = 1
        private const val OTP_MAX_SIZE = 4
    }
}