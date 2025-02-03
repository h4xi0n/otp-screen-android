package com.h4xi0n.otp.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.text.Editable
import android.text.InputType
import android.util.AttributeSet
import android.view.Gravity
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.h4xi0n.otp.R

class OTPView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    interface OnOTPCompleteListener {
        fun onOTPComplete(otp: String)
        fun onResetRequested()
    }

    private var otpLength = 6
    private val editTexts = mutableListOf<EditText>()
    var onOTPCompleteListener: OnOTPCompleteListener? = null

    init {
        orientation = HORIZONTAL
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.OTPView)
            otpLength = typedArray.getInt(R.styleable.OTPView_length, 6)
            typedArray.recycle()
        }
        setupViews()
    }

    private fun setupViews() {
        removeAllViews()
        editTexts.clear()

        val params = LayoutParams(
            dpToPx(48),
            dpToPx(48)
        ).apply {
            setMargins(dpToPx(4), 0, dpToPx(4), 0)
        }

        repeat(otpLength) { index ->
            val editText = EditText(context).apply {
                layoutParams = params
                inputType = InputType.TYPE_CLASS_NUMBER
                maxLines = 1
                gravity = Gravity.CENTER
                background = createEditTextBackground()
                addTextChangedListener(OTPTextWatcher(index))
            }
            addView(editText)
            editTexts.add(editText)
        }
    }

    private fun createEditTextBackground(): Drawable {
        return GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = dpToPx(8).toFloat()
            setStroke(dpToPx(2), ContextCompat.getColor(context, R.color.black))
        }
    }

    private inner class OTPTextWatcher(private val currentIndex: Int) : TextWatcherAdapter() {
        override fun afterTextChanged(s: Editable?) {
            if (s?.length == 1 && currentIndex < otpLength - 1) {
                editTexts[currentIndex + 1].requestFocus()
            } else if (s.isNullOrEmpty() && currentIndex > 0) {
                editTexts[currentIndex - 1].requestFocus()
            }
            checkCompletion()
        }
    }

    private fun checkCompletion() {
        val otp = editTexts.joinToString("") { it.text.toString() }
        if (otp.length == otpLength) {
            onOTPCompleteListener?.onOTPComplete(otp)
        }
    }

    fun reset() {
        editTexts.forEach { it.text.clear() }
        editTexts.first().requestFocus()
    }

    private fun dpToPx(dp: Int): Int =
        (dp * resources.displayMetrics.density).toInt()
}

abstract class TextWatcherAdapter : android.text.TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(s: Editable?) {}
}