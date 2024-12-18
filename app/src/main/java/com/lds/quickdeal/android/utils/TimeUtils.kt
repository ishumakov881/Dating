package com.lds.quickdeal.android.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class TimeUtils {


    
    companion object{

        private val DATE_PATTERN: String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

        fun formatDate(dateString: String): String {
            return try {
                val outputPattern = "dd.MM.yyyy HH:mm"
                val inputFormat = java.text.SimpleDateFormat(DATE_PATTERN, java.util.Locale.getDefault())
                inputFormat.timeZone = java.util.TimeZone.getTimeZone("UTC") // Обрабатываем Z как UTC
                val outputFormat = java.text.SimpleDateFormat(outputPattern, java.util.Locale.getDefault())
                val date = inputFormat.parse(dateString)
                outputFormat.format(date!!)
            } catch (e: Exception) {
                "Неверный формат даты"
            }
        }


        fun nowTimeFormatted(): String {
            val dateFormat = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())
            dateFormat.timeZone = TimeZone.getTimeZone("UTC")
            return dateFormat.format(Date())
        }
    }
}