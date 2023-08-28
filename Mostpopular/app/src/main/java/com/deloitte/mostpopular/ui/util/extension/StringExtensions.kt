package com.deloitte.mostpopular.ui.util.extension

import java.text.SimpleDateFormat
import java.util.*



/**
 * convert Pattern: yyyy-MM-dd  to data
 */
fun String.parseDateString(): Date? {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return try {
        formatter.parse(this)
    } catch (e: Exception) {
        null
    }
}