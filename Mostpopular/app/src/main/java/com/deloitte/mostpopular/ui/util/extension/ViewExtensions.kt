package com.deloitte.mostpopular.ui.util.extension
import android.content.Context
import android.os.SystemClock
import android.view.View
import android.view.inputmethod.InputMethodManager

private const val DEFAULT_CLICK_THRESHOLD_MS = 1000L


fun View.setOnSingleClickListener(
    threshold: Long = DEFAULT_CLICK_THRESHOLD_MS,
    action: (View) -> Unit
) {
    var lastClickTime = 0L

    setOnClickListener {
        val currentTime = SystemClock.elapsedRealtime()
        if (currentTime - lastClickTime > threshold) {
            lastClickTime = currentTime
            action(this)
        }
    }
}

fun View.visibility(show:Boolean){
    visibility = if(show) View.VISIBLE else View.GONE
}

fun View.hideKeyboard() {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    inputMethodManager?.hideSoftInputFromWindow(windowToken, 0)
}