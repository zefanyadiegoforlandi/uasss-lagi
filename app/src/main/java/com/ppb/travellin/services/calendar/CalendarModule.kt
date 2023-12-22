package com.ppb.travellin.services.calendar

object CalendarModule {

    fun getIntervalDay(day: Int, month: Int, year: Int): Int {
        val calendar = java.util.Calendar.getInstance()
        val currentYear = calendar.get(java.util.Calendar.YEAR)
        val currentMonth = calendar.get(java.util.Calendar.MONTH)
        val currentDay = calendar.get(java.util.Calendar.DAY_OF_MONTH)

        if (year == currentYear && month == currentMonth) {
            return day - currentDay
        } else if (year == currentYear) {
            return day + (month - currentMonth)*30 - currentDay
        } else if (year > currentYear) {
            return day + (month - currentMonth)*30 + (year - currentYear)*365 - currentDay
        } else {
            return -1
        }

    }



}