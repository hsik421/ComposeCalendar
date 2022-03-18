package com.mobile.app.composecalendar

import java.util.*

data class CalendarModel(
    var year: Int,
    var month: Int,
    var days: List<Date>
)

val defaultCalendar = CalendarModel(
    month = Calendar.getInstance(Locale.KOREA).get(Calendar.MONTH),
    year = Calendar.getInstance(Locale.KOREA).get(Calendar.YEAR),
    days = emptyList()
)

data class Date(
    var dateState: DateEnum,
    val calendarState: CalendarEnum,
    val day: Int
)

enum class CalendarEnum {
    NONE,
    TODAY,
    CURRENT
}

enum class DateEnum {
    NONE,
    TODAY,
    SELECT
}