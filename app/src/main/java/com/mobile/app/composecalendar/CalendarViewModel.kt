package com.mobile.app.composecalendar

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CalendarViewModel : ViewModel() {
    private val _date = MutableStateFlow(defaultCalendar)
    val date: StateFlow<CalendarModel> get() = _date

    private val _repeatDay = MutableStateFlow(0)
    val repeatDay: StateFlow<Int> get() = _repeatDay


    init {
        getData()
    }

    private fun getData() {
        viewModelScope.launch(Dispatchers.Default) {
            val current = CalendarModule.currentCalendar()
            _date.value = CalendarModule.dayData(month = current.first, year = current.second)
        }
    }

    fun onNext(repeatDay : Int = 0) {
        viewModelScope.launch(Dispatchers.Default) {
            val next = CalendarModule.nextDate(
                month = _date.value.month,
                year = _date.value.year
            )
            setDateViewState(next,repeatDay)
        }
    }

    fun onPrev(repeatDay : Int = 0) {
        viewModelScope.launch(Dispatchers.Default) {
            val prev = CalendarModule.prevDate(
                month = _date.value.month,
                year = _date.value.year
            )
            setDateViewState(prev,repeatDay)
        }
    }

    fun repeatValue(value : Int) : Int{
        val repeat = _repeatDay.value.xor(value)
        _repeatDay.value = repeat
        return repeat
    }

    fun setDateViewState(date : Pair<Int,Int>, repeatDay : Int = 0){
        _date.value = CalendarModule.dayData(month = date.first, year = date.second).also {
            it.days = setRepeatDate(it.days,repeatDay)
        }
    }

    private fun setRepeatDate(days : List<Date>,repeatDay : Int) : List<Date>{
        days.forEach {
            if(it.dateState == DateEnum.SELECT){
                it.dateState = DateEnum.NONE
            }
        }

        var todayIndex = days.indexOf(days.find { it.dateState == DateEnum.TODAY })

        days.forEachIndexed { index, date ->
            if(date.dateState == DateEnum.TODAY){
                todayIndex = index
            }
            val weekIndex = 6 - (index % 7)
            if(repeatDay.or(1.shl(weekIndex)) == repeatDay && (todayIndex < index || todayIndex < 0 )){
                date.dateState = DateEnum.SELECT
            }
        }
        return days
    }
}