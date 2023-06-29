package com.example.letterboxf.utils

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView


class NonScrollableRecyclerView : RecyclerView {
    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context!!, attrs, defStyle
    )

    override fun onTouchEvent(e: MotionEvent): Boolean {
        // Do nothing to disable touch events
        return false
    }

    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        // Do nothing to disable touch events
        return false
    }
}