package com.h4xi0n.otp.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp


/**
 * A composable function for OTP (One-Time Password) entry.
 *
 * @param modifier The modifier to be applied to the Row containing the OTP fields.
 * @param otpLength The length of the OTP to be entered. Default is 6.
 * @param onOtpComplete A callback function to be invoked when the OTP entry is complete.
 */
@Composable
fun OtpEntry(
    modifier: Modifier = Modifier,
    otpLength: Int = 6,
    onOtpComplete: (String) -> Unit,
) {
    val otpValues = remember { mutableStateListOf<TextFieldValue>().apply {
        repeat(otpLength) { add(TextFieldValue("")) }
    } }
    val focusManager = LocalFocusManager.current

    Row(modifier = modifier) {
        repeat(otpLength) { index ->
            BasicTextField(
                value = otpValues[index],
                onValueChange = { newValue ->
                    if (newValue.text.length <= 1 && newValue.text.all { it.isDigit() }) {
                        otpValues[index] = newValue

                        // Auto-focus next
                        if (newValue.text.isNotEmpty() && index < otpLength - 1) {
                            focusManager.moveFocus(FocusDirection.Right)
                        }

                        // Check if all fields are filled
                        if (otpValues.all { it.text.isNotEmpty() }) {
                            onOtpComplete(otpValues.joinToString("") { it.text })
                        }
                    }
                },
                modifier = Modifier
                    .size(50.dp)
                    .border(1.dp, Color.Gray, MaterialTheme.shapes.small),
                keyboardOptions = KeyboardOptions(
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Number
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onOtpComplete(otpValues.joinToString("") { it.text }) },
                    onPrevious = { focusManager.moveFocus(FocusDirection.Left) },
                    onNext = { focusManager.moveFocus(FocusDirection.Right) }
                ),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            )
        }
    }
}