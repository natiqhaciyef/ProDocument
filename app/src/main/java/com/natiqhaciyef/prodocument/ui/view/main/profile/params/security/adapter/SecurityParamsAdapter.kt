package com.natiqhaciyef.prodocument.ui.view.main.profile.params.security.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import com.natiqhaciyef.core.base.ui.BaseRecyclerViewAdapter
import com.natiqhaciyef.common.R
import com.natiqhaciyef.common.constants.EIGHTEEN
import com.natiqhaciyef.common.constants.SOMETHING_WENT_WRONG
import com.natiqhaciyef.common.constants.ZERO
import com.natiqhaciyef.prodocument.databinding.RecyclerParamsItemBinding
import com.natiqhaciyef.prodocument.ui.manager.FingerPrintManager
import com.natiqhaciyef.prodocument.ui.manager.RememberUserManager
import com.natiqhaciyef.prodocument.ui.view.main.profile.params.model.FieldType
import com.natiqhaciyef.prodocument.ui.view.main.profile.params.model.ParamsUIModel

class SecurityParamsAdapter(
    private val activity: AppCompatActivity,
    securityParamsList: MutableList<ParamsUIModel>,
) : BaseRecyclerViewAdapter<ParamsUIModel, RecyclerParamsItemBinding>(securityParamsList) {
    private var isChecked = false
    override val binding: (Context, ViewGroup, Boolean) -> RecyclerParamsItemBinding =
        { ctx, vg, bool ->
            RecyclerParamsItemBinding.inflate(LayoutInflater.from(ctx), vg, bool)
        }
    var onClickAction: (ParamsUIModel) -> Unit = {

    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = list[position]

        holder.binding.let {
            lockConfig(it, item)

            when (item.fieldType) {
                FieldType.SWITCH -> {
                    switchConfig(item, it)
                }

                FieldType.NAVIGATION -> {
                    navigationConfig(item, it)
                }

                else -> {}
            }
        }

        holder.itemView.setOnClickListener {
            securityProcessConfiguration(item)
        }
    }


    private fun switchConfig(
        item: ParamsUIModel,
        binding: RecyclerParamsItemBinding,
        action: () -> Unit = {}
    ) {
        with(binding) {
            switchIcon.visibility = View.VISIBLE
            preferenceLine.visibility = View.GONE
            goDetailsIcon.visibility = View.GONE

            val params = preferenceTitle.layoutParams as ConstraintLayout.LayoutParams
            params.endToStart = switchIcon.id
            params.topToTop = preferenceLayout.id

            preferenceTitle.text = item.title
            preferenceTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, EIGHTEEN.toFloat())
            preferenceTitle.setTextColor(ContextCompat.getColor(activity, R.color.grayscale_900))
            switchIcon.setOnCheckedChangeListener { compoundButton, b ->
                isChecked = !isChecked
                action.invoke()
            }
        }
    }

    private fun navigationConfig(
        item: ParamsUIModel,
        binding: RecyclerParamsItemBinding,
        action: () -> Unit = {}
    ) {
        with(binding) {
            goDetailsIcon.visibility = View.VISIBLE
            switchIcon.visibility = View.GONE
            preferenceLine.visibility = View.GONE

            val params = preferenceTitle.layoutParams as ConstraintLayout.LayoutParams
            params.endToStart = goDetailsIcon.id
            params.topToTop = preferenceLayout.id

            preferenceTitle.text = item.title
            preferenceTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, EIGHTEEN.toFloat())
            preferenceTitle.setTextColor(ContextCompat.getColor(activity, R.color.grayscale_900))
            goDetailsIcon.setOnClickListener { action.invoke() }
        }
    }

    private fun lockConfig(
        binding: RecyclerParamsItemBinding,
        item: ParamsUIModel
    ) {
        if (!item.isAvailableEveryone) {
            with(binding) {
                lockIcon.visibility = View.VISIBLE
                val lockParams = lockIcon.layoutParams as ConstraintLayout.LayoutParams


                when (item.fieldType) {
                    FieldType.SWITCH -> {
                        val id = switchIcon.id
                        lockParams.endToStart = id
                        switchIcon.isEnabled = false
                    }

                    FieldType.NAVIGATION -> {
                        val id = goDetailsIcon.id
                        lockParams.endToStart = id
                        goDetailsIcon.isEnabled = false
                    }

                    FieldType.SPACE -> {
                        val params = preferenceTitle.layoutParams as ConstraintLayout.LayoutParams
                        params.endToStart = lockIcon.id
                        lockParams.endToEnd = preferenceLayout.id
                    }

                    else -> {
                        preferenceLayout.layoutParams as ConstraintLayout.LayoutParams
                    }
                }
            }
        }
    }

    private fun securityProcessConfiguration(paramsUIModel: ParamsUIModel) =
        when (paramsUIModel.title) {
            activity.getString(R.string.remember_me_param) -> {
                RememberUserManager.rememberState(activity, isChecked)
            }

            activity.getString(R.string.biometric_id) -> {
                if (FingerPrintManager.isBiometricReady(activity)) {
                    FingerPrintManager.showBiometricPrompt(
                        title = activity.getString(R.string.biometric_id),
                        subtitle = activity.getString(R.string.biometric_subtitle),
                        description = activity.getString(R.string.biometric_details),
                        activity = activity
                    ) { isSucceed, exception ->
                        // store biometric enabled and add it to user login screen
                    }
                } else {

                }
            }

            activity.getString(R.string.sms_authenticator) -> {}

            activity.getString(R.string.google_authenticator) -> {}

            activity.getString(R.string.device_management) -> {
                activity.startActivity(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts(
                            activity.getString(R.string.package_),
                            activity.packageName,
                            null
                        )
                    )
                )
            }

            else -> {}
        }
}