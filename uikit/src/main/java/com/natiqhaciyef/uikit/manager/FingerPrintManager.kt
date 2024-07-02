package com.natiqhaciyef.uikit.manager

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.natiqhaciyef.common.constants.ResultExceptions


object FingerPrintManager {

    private fun hasBiometricCapability(context: Context): Int {
        return BiometricManager.from(context).canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
    }

    fun isBiometricReady(context: Context) =
        hasBiometricCapability(context) == BiometricManager.BIOMETRIC_SUCCESS

    private fun setBiometricPromptInfo(
        title: String,
        subtitle: String,
        description: String,
    ): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setDescription(description)
            .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
            .build()
    }

    private fun initBiometricPrompt(
        activity: AppCompatActivity,
        listener: (Boolean, Exception?) -> Unit = { isSucceed, exception -> }
    ): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(activity)
        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                listener.invoke(
                    false,
                    ResultExceptions.BiometricAuthenticationError(
                        msg = errorCode.toString(),
                        errorCode = errorCode
                    )
                )
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                listener.invoke(
                    false,
                    ResultExceptions.BiometricAuthenticationError()
                )
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                listener.invoke(
                    true,
                    ResultExceptions.BiometricAuthenticationError()
                )
            }
        }

        return BiometricPrompt(activity, executor, callback)
    }

    fun showBiometricPrompt(
        title: String,
        subtitle: String,
        description: String,
        activity: AppCompatActivity,
        cryptoObject: BiometricPrompt.CryptoObject? = null,
        listener: (Boolean, Exception?) -> Unit,
    ) {
        val promptInfo = setBiometricPromptInfo(
            title,
            subtitle,
            description,
        )

        val biometricPrompt = initBiometricPrompt(activity, listener)
        biometricPrompt.apply {
            if (cryptoObject == null) authenticate(promptInfo)
            else authenticate(promptInfo, cryptoObject)
        }
    }
}