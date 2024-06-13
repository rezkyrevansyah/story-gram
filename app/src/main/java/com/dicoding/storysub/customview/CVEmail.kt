package com.dicoding.storysub.customview

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.google.android.material.textfield.TextInputEditText
import com.dicoding.storysub.R

class CVEmail @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextInputEditText(context, attrs), View.OnTouchListener {

    init {
        setOnTouchListener(this)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = context.getString(R.string.hint_masukkan_email)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    override fun onTextChanged(cs: CharSequence?, start: Int, before: Int, count: Int) {
        super.onTextChanged(cs, start, before, count)
        if (cs != null && cs.isNotEmpty()) {
            val emailText = cs.toString()
            error = when {
                !emailText.contains("@") -> context.getString(R.string.error_wrong_email)
                !emailText.endsWith(".com") && !emailText.endsWith(".co.id") -> context.getString(R.string.error_wrong_email)
                else -> null
            }
        } else {
            error = context.getString(R.string.error_empty_email)
        }
    }

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        return false
    }
}


