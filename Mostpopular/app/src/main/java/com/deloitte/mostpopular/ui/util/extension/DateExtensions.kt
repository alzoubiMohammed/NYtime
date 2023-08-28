package com.deloitte.mostpopular.ui.util.extension

import android.icu.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


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



