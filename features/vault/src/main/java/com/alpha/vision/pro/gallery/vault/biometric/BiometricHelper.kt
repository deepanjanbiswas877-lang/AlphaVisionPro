package com.alpha.vision.pro.gallery.vault.biometric

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

/**
 * Encapsulates BiometricPrompt setup and result callbacks.
 * Uses BIOMETRIC_STRONG | DEVICE_CREDENTIAL — fallback to PIN/pattern.
 */
object BiometricHelper {

    fun canAuthenticate(context: Context): Boolean {
        val manager = BiometricManager.from(context)
        return manager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL) ==
                BiometricManager.BIOMETRIC_SUCCESS
    }

    fun authenticate(
        activity   : FragmentActivity,
        title      : String = "Unlock Vault",
        subtitle   : String = "Use biometric or device PIN",
        onSuccess  : () -> Unit,
        onFailure  : (String) -> Unit
    ) {
        val executor = ContextCompat.getMainExecutor(activity)
        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                onSuccess()
            }
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                onFailure(errString.toString())
            }
            override fun onAuthenticationFailed() {
                onFailure("Authentication failed. Try again.")
            }
        }

        val prompt = BiometricPrompt(activity, executor, callback)
        val info   = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
            .build()
        prompt.authenticate(info)
    }
}
