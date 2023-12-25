package com.beni.core.data.local.models

import com.google.api.services.calendar.model.Events

data class MCalendar(
    val title: String,
    val img: Int,
    val days: List<String>,
    val events: List<Events>? = null,
)
