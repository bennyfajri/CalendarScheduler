package com.beni.core.data.local.models

data class CalendarEventModel(
    var id: Int,
    var title: String? = "",
    var description: String? = "",
    var startDate: String? = "",
    var endDate: String? = "",
)
