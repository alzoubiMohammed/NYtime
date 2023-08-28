package com.deloitte.mostpopular.ui.util.extension

import android.icu.text.SimpleDateFormat
import com.deloitte.mostpopular.R
import java.util.Date
import java.util.Locale
import android.content.Context
import java.util.*

/**
 * convert Pattern: dd/MM/yyyy to string
 */
fun Date.asBirthDataStringFormat():String{
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return try{
         dateFormat.format(time)
    }catch (e:Exception){
        e.printStackTrace()
         ""
    }

}

fun Date.getTimeAgo(context: Context): String {
    val currentTime = Calendar.getInstance().time
    val timeDifference = currentTime.time - this.time
    val seconds = timeDifference / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24
    val weeks = days / 7
    val months = days / 30
    val years = days / 365

    return when {
        seconds < 60 -> context.getString(R.string.time_ago_seconds, seconds)
        minutes < 60 -> context.getString(R.string.time_ago_minutes, minutes)
        hours < 24 -> context.getString(R.string.time_ago_hours, hours)
        days < 7 -> context.getString(R.string.time_ago_days, days)
        weeks < 4 -> context.getString(R.string.time_ago_weeks, weeks)
        months < 12 -> context.getString(R.string.time_ago_months, months)
        else -> context.getString(R.string.time_ago_years, years)
    }
}

