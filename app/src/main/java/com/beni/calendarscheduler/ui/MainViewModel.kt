package com.beni.calendarscheduler.ui

import androidx.lifecycle.ViewModel
import com.beni.core.data.SampleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: SampleRepository
) : ViewModel() {

}