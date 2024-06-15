package com.natiqhaciyef.prodocument.ui.custom

import android.app.DatePickerDialog
import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.natiqhaciyef.common.R
import androidx.core.widget.doOnTextChanged
import com.natiqhaciyef.common.objects.ErrorMessages
import com.natiqhaciyef.prodocument.databinding.CustomInputViewBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CustomInputView(
    private val ctx: Context,
    private val attributeSet: AttributeSet
) : ConstraintLayout(ctx, attributeSet) {
    private var binding: CustomInputViewBinding? = null
    private var currentSelectedTime: Long = 0L

    init {
        binding = CustomInputViewBinding.inflate(LayoutInflater.from(ctx), this, true)
        defaultInit()
    }

    fun initCustomView(title: String, inputHint: String) {
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
        "0x1" -> InputType.TYPE_CLASS_TEXT
        "0x2" -> InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS + InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS
        "0x3" -> InputType.TYPE_CLASS_NUMBER
        "0x4" -> InputType.TYPE_CLASS_NUMBER + InputType.TYPE_NUMBER_FLAG_DECIMAL + InputType.TYPE_NUMBER_FLAG_SIGNED
        "0x5" -> InputType.TYPE_NULL
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

        if (customInputType == "0x5") {
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
        val format = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(format, Locale.UK)
        val date = sdf.format(calendar.time)
        if (calendar.time.time < System.currentTimeMillis())
            binding?.customInputEditText?.setText(date)
        else
            binding?.customInputEditText?.setText(ErrorMessages.DATE_OVER_FLOW_ERROR)
    }
}