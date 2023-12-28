package com.beni.calendarscheduler.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.beni.core.data.CalendarSchedularRepository
import com.beni.core.data.local.models.MCalendar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: CalendarSchedularRepository
) : ViewModel() {
    private lateinit var _dateList: MutableLiveData<List<MCalendar>>
    val dateList: LiveData<List<MCalendar>> get() = _dateList

    val isLoading = repository.isLoading

    init {
        getDateListFromRepo()
    }

    fun getDateListFromRepo() = viewModelScope.launch {
            _dateList = repository.getDateList().asLiveData() as MutableLiveData<List<MCalendar>>
    }

    fun setEvent(newMCalendar: List<MCalendar>) = viewModelScope.launch {
        _dateList.value = newMCalendar
    }
}