package com.beni.core.data


import com.beni.core.data.local.models.MCalendar
import com.beni.core.util.ConstantFunction
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class CalendarSchedularRepository @Inject constructor() {
    fun getDateList(): Flow<List<MCalendar>> = flow {
        val listMCalendar = ArrayList<MCalendar>()
        for (year in ConstantFunction.firstYear()..ConstantFunction.lastYear()) {
            for (month in 1..12) {
                listMCalendar.add(findDatesInMonth(year, month))
            }
        }
        emit(listMCalendar as List<MCalendar>)
    }.catch {
        emit(emptyList())
    }.flowOn(Dispatchers.IO)

    private fun findDatesInMonth(year: Int, month: Int): MCalendar {
        val dayList = ArrayList<String>()
        val calendar = Calendar.getInstance()
        val smallSdf = SimpleDateFormat("d-MMM", Locale.getDefault())
        val titleSdf = SimpleDateFormat("MMMM, yyyy", Locale.getDefault())
        val fullSdf = SimpleDateFormat("d-MMM, yyyy", Locale.getDefault())

        calendar.clear()
        calendar.set(year, month - 1, 1)

        val title = titleSdf.format(calendar.time)
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        while ((calendar.get(Calendar.DAY_OF_MONTH) < daysInMonth) && (calendar.get(Calendar.MONTH) == month - 1)) {
            //for find monday
            while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }

            val monTime = calendar.time
            var days = calendar.get(Calendar.DAY_OF_MONTH).toString()
            val mon = CalendarDay.from(year, month, calendar.get(Calendar.DAY_OF_MONTH))

            //for find sunday
            while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }

            if (mon.year != calendar.get(Calendar.YEAR)) {
                days = "${fullSdf.format(monTime)} ― ${fullSdf.format(calendar.time)}"
            } else {
                if (mon.month != calendar.get(Calendar.MONTH) + 1) {
                    days = "${smallSdf.format(monTime)} ― ${fullSdf.format(calendar.time)}"
                } else {
                    days += " ― ${fullSdf.format(calendar.time)}"
                }
            }

            dayList.add(days)
        }
        return MCalendar(
            ConstantFunction.upperFirst(title),
            ConstantFunction.getMonthImage(month),
            dayList
        )
    }
}