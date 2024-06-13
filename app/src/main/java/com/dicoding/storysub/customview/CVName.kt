package com.dicoding.storysub.customview

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.google.android.material.textfield.TextInputEditText
import com.dicoding.storysub.R

class CVName @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextInputEditText(context, attrs), View.OnTouchListener {

    init {
        setOnTouchListener(this)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = context.getString(R.string.hint_masukkan_nama)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    override fun onTextChanged(cs: CharSequence?, start: Int, before: Int, count: Int) {
        super.onTextChanged(cs, start, before, count)
        error = when {
            cs.isNullOrEmpty() -> context.getString(R.string.error_empty_name)
            else -> null
        }
    }

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        return false
    }
}
