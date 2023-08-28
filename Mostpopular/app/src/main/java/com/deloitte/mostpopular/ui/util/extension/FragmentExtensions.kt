package com.deloitte.mostpopular.ui.util.extension

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.util.Date

fun Fragment.showToast(message: String?) {
    Toast.makeText(this.activity, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.showDatePicker(
    onDateSelected: (result: Date) -> Unit
) {

    val currentDate = Calendar.getInstance()
    val initialYear = currentDate.get(Calendar.YEAR)
    val initialMonth = currentDate.get(Calendar.MONTH)
    val initialDay = currentDate.get(
        Calendar.DAY_OF_MONTH)
    val datePickerDialog = DatePickerDialog(
        requireContext(),
        { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)
            onDateSelected(selectedDate.time)
        },
        initialYear,
        initialMonth,
        initialDay
    )
    datePickerDialog.show()
}
