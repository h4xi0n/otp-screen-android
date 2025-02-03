package com.h4xi0n.otp.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

internal class OtpSdk {
    companion object {
        private const val KEY_ALIAS = "OTP_ENCRYPTION_KEY"
        private const val AES_MODE = "AES/GCM/NoPadding"
        private const val TAG_LENGTH = 128

        private fun getOrCreateSecretKey(): SecretKey {
            val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }

            return if (keyStore.containsAlias(KEY_ALIAS)) {
                (keyStore.getEntry(KEY_ALIAS, null) as KeyStore.SecretKeyEntry).secretKey
            } else {
                KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore").apply {
                    init(
                        KeyGenParameterSpec.Builder(
                            KEY_ALIAS,
                            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                        )
                            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                            .build()
                    )
                }.generateKey()
            }
        }


        fun decryptOtp(encryptedData: String): String {
            val parts = encryptedData.split(":")
            val cipher = Cipher.getInstance(AES_MODE)
            val spec = GCMParameterSpec(TAG_LENGTH, Base64.decode(parts[1], Base64.DEFAULT))
            cipher.init(Cipher.DECRYPT_MODE, getOrCreateSecretKey(), spec)
            return String(cipher.doFinal(Base64.decode(parts[0], Base64.DEFAULT)), Charsets.UTF_8)
        }

        fun encryptOtp(otp: String): String {
            val cipher = Cipher.getInstance(AES_MODE)
            cipher.init(Cipher.ENCRYPT_MODE, getOrCreateSecretKey())
            val iv = cipher.iv
            val encryptedBytes = cipher.doFinal(otp.toByteArray(Charsets.UTF_8))
            return Base64.encodeToString(encryptedBytes, Base64.DEFAULT) + ":" +
                    Base64.encodeToString(iv, Base64.DEFAULT)
        }

        fun generateOtp(length: Int = 6): String {
            val allowedChars = ('0'..'9')
            return (1..length)
                .map { allowedChars.random() }
                .joinToString("")
        }
    }
}