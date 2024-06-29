package com.natiqhaciyef.prodocument.ui.custom

import android.R.attr.maxLength
import android.app.DatePickerDialog
import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.natiqhaciyef.common.R
import com.natiqhaciyef.common.constants.DATE_OVER_FLOW_ERROR
import com.natiqhaciyef.common.constants.EMPTY_STRING
import com.natiqhaciyef.common.constants.ZERO
import com.natiqhaciyef.prodocument.databinding.CustomInputViewBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class CustomInputView(
    private val ctx: Context,
    private val attributeSet: AttributeSet
) : ConstraintLayout(ctx, attributeSet) {
    private var binding: CustomInputViewBinding? = null
    private var currentSelectedTime: Long = ZERO.toLong()

    init {
        binding = CustomInputViewBinding.inflate(LayoutInflater.from(ctx), this, true)
        defaultInit()
    }

    fun initCustomView(
        title: String,
        inputHint: String,
        inputData: String? = null,
    ) {
        binding?.let {
            it.customTitleText.text = title
            it.customInputEditText.hint = inputHint

            if (inputData != null)
                it.customInputEditText.setText(inputData)
        }
    }

    fun listenUserInput(action: (CharSequence?, Int, Int, Int) -> Unit) {
        binding?.customInputEditText?.doOnTextChanged(action)
    }

    fun listenUserInputWithAddTextWatcher(textWatcher: TextWatcher) {
        binding?.customInputEditText?.addTextChangedListener(textWatcher)
    }

    fun listenUserInputWithRemoveTextWatcher(textWatcher: TextWatcher) {
        binding?.customInputEditText?.removeTextChangedListener(textWatcher)
    }

    fun setMaxLength(length: Int) {
        binding?.customInputEditText?.filters = arrayOf<InputFilter>(LengthFilter(length))
    }

    fun insertInput(inputData: String, title: String? = null) {
        if (title != null)
            binding?.customTitleText?.text = title
        binding?.customInputEditText?.setText(inputData)
    }

    fun parseSelection(inputLength: Int) {
        binding?.customInputEditText?.setSelection(inputLength)
    }

    fun getInputResult(): String {
        return binding?.customInputEditText?.text?.toString() ?: EMPTY_STRING
    }

    fun getEditableText(): Editable {
        return binding!!.customInputEditText.editableText
    }

    private fun getCustomInputType(inpType: String) = when (inpType) {
        TEXT_TYPE -> InputType.TYPE_CLASS_TEXT
        EMAIL_TYPE -> InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS + InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS
        NUMBER_TYPE -> InputType.TYPE_CLASS_NUMBER
        DECIMAL_TYPE -> InputType.TYPE_CLASS_NUMBER + InputType.TYPE_NUMBER_FLAG_DECIMAL + InputType.TYPE_NUMBER_FLAG_SIGNED
        NULL_TYPE -> InputType.TYPE_NULL
        else -> InputType.TYPE_CLASS_TEXT
    }

    private fun defaultInit() {
        val calendar = Calendar.getInstance()
        val typedArray = ctx.obtainStyledAttributes(
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

        if (customInputType == NULL_TYPE) {
            binding?.customInputEditText?.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                ContextCompat.getDrawable(ctx, R.drawable.calendar_icon),
                null
            )

            binding?.customInputEditText?.setOnClickListener {
                datePickerDialog(calendar)
            }
        }
        customInputType?.let {
            val inputType = getCustomInputType(it)
            binding?.customInputEditText?.inputType = inputType
        }
    }

    private fun datePickerDialog(calendar: Calendar) {
        val listener = DatePickerDialog.OnDateSetListener { datePicker, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            changeCalendar(calendar)
        }

        binding?.let {
            it.customInputEditText.setOnClickListener {
                val datePickerDialog = DatePickerDialog(
                    ctx,
                    listener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                )
                datePickerDialog.datePicker.maxDate = Calendar.getInstance().timeInMillis
                datePickerDialog.show()
            }
        }
    }

    private fun changeCalendar(calendar: Calendar) {
        currentSelectedTime = calendar.timeInMillis
        val sdf = SimpleDateFormat(format, Locale.UK)
        val date = sdf.format(calendar.time)
        if (calendar.time.time < System.currentTimeMillis())
            binding?.customInputEditText?.setText(date)
        else
            binding?.customInputEditText?.setText(DATE_OVER_FLOW_ERROR)
    }

    companion object {
        const val format = "dd/MM/yyyy"
        const val TEXT_TYPE = "0x1"
        const val EMAIL_TYPE = "0x2"
        const val NUMBER_TYPE = "0x3"
        const val DECIMAL_TYPE = "0x4"
        const val NULL_TYPE = "0x5"
    }
}