package com.mobile.app.composecalendar

import java.text.DateFormatSymbols
import java.util.*

object CalendarModule {

    val WEEK_LABEL = arrayOf("Sun" to 64, "Mon" to 32, "Tue" to 16, "Wed" to 8, "Thu" to 4, "Fri" to 2, "Sat" to 1)

    fun dayData(month: Int, year: Int): CalendarModel {

        val calendar = Calendar.getInstance(Locale.KOREA)

        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentYear = calendar.get(Calendar.YEAR)

        calendar.firstDayOfWeek = Calendar.SUNDAY
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        val startWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1
        val before = prevDate(month, year)
        val beforeEndDate = getDaysInMonth(month = before.first, year = before.second)
        val currentEndDate = getDaysInMonth(month = month, year = year)
        val days = mutableListOf<Date>()

        if (startWeek != 0) {
            days.addAll((startWeek - 1 downTo 0).map { Date(DateEnum.NONE, CalendarEnum.NONE,beforeEndDate - it) })
        }

        days.addAll((1..currentEndDate).map {
            if(currentMonth == month && currentDay == it && currentYear == year){
                Date(DateEnum.TODAY,CalendarEnum.TODAY, it)
            }else{
                Date(DateEnum.NONE,CalendarEnum.CURRENT, it)
            }
        })

        if (days.size % 7 != 0) {
            (0 until 7 - (days.size % 7)).forEach {
                days.add(Date(DateEnum.NONE, CalendarEnum.NONE,it + 1))
            }
        }

//        Log.i("hsik", "test[$startWeek][$month] = $days")
        return CalendarModel(year = year, month = month, days = days)
    }

    fun prevDate(month: Int, year: Int): Pair<Int, Int> {
        return when (month) {
            Calendar.JANUARY -> {
                Pair(Calendar.DECEMBER, year - 1)
            }
            else -> {
                Pair(month - 1, year)
            }
        }
    }

    fun nextDate(month: Int, year: Int): Pair<Int, Int> {
        return when (month) {
            Calendar.DECEMBER -> {
                Pair(Calendar.JANUARY, year + 1)
            }
            else -> {
                Pair(month + 1, year)
            }
        }
    }

    fun currentCalendar(): Pair<Int, Int> {
        val calendar = Calendar.getInstance(Locale.KOREA)
        return Pair(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR))
    }

    private fun getDaysInMonth(month: Int, year: Int): Int {
//        Log.i("hsik", "month[$month]  year[$year]")
        return when (month) {
            Calendar.JANUARY, Calendar.MARCH, Calendar.MAY, Calendar.JULY, Calendar.AUGUST, Calendar.OCTOBER, Calendar.DECEMBER -> 31
            Calendar.APRIL, Calendar.JUNE, Calendar.SEPTEMBER, Calendar.NOVEMBER -> 30
            Calendar.FEBRUARY -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
            else -> throw IllegalArgumentException("Invalid Month")
        }
    }
    fun getMonthText() : Array<String>?{
        return DateFormatSymbols(Locale.getDefault()).months
    }
}