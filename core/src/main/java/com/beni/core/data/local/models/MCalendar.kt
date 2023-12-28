package com.beni.core.data.local.models

import com.google.api.services.calendar.model.Event

data class MCalendar(
    val title: String,
    val img: Int,
    val days: List<String>,
    var events: List<Event>? = null,
)
