package com.h4xi0n.otp.manager

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.h4xi0n.otp.security.OtpSdk
import com.h4xi0n.otp.ui.OtpEntry



class OtpManager {
    companion object {

        /**
         * Generates a One-Time Password (OTP) of the specified length.
         *
         * @param length The length of the OTP to be generated. Default is 6.
         * @return A string representing the generated OTP.
         */
        fun generateOtp(length: Int = 6) = OtpSdk.generateOtp(length)

        /**
         * Encrypts the given OTP using AES/GCM/NoPadding.
         *
         * @param otp The OTP to be encrypted.
         * @return A string representing the encrypted OTP and initialization vector (IV), separated by a colon.
         */
        fun encryptOtp(otp: String) = OtpSdk.encryptOtp(otp)

        /**
         * Decrypts the given encrypted OTP data.
         *
         * @param encryptedData The encrypted OTP data, containing the encrypted OTP and IV separated by a colon.
         * @return A string representing the decrypted OTP.
         */
        fun decryptOtp(encryptedData: String) = OtpSdk.decryptOtp(encryptedData)
    }

    /**
     * A composable function for OTP (One-Time Password) entry.
     *
     * @param modifier The modifier to be applied to the Row containing the OTP fields.
     * @param otpLength The length of the OTP to be entered. Default is 6.
     * @param onOtpComplete A callback function to be invoked when the OTP entry is complete.
     * @param onResetRequest A callback function to be invoked when a reset is requested. Default is an empty implementation.
     */
    @Composable
    fun GetOtpComposable(modifier: Modifier = Modifier,
                         otpLength: Int = 6,
                         onOtpComplete: (String) -> Unit) = OtpEntry(modifier, otpLength, onOtpComplete)
}