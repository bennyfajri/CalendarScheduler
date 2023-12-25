package com.beni.calendarscheduler.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.beni.core.data.CalendarSchedularRepository
import com.beni.core.data.local.models.MCalendar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: CalendarSchedularRepository
) : ViewModel() {
    private lateinit var _dateList: LiveData<List<MCalendar>>
    val dateList: LiveData<List<MCalendar>> get() = _dateList

    val isLoading = MutableLiveData(true)

    init {
        getDateListFromRepo()
    }

    fun getDateListFromRepo() = viewModelScope.launch {
        if (!::_dateList.isInitialized) {
            _dateList = repository.getDateList().asLiveData()
            delay(1000)
            isLoading.value = false
        }
    }
}