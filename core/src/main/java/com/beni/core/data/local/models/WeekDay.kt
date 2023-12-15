package com.beni.core.data.local.models

import java.io.Serializable
import java.time.LocalDate
import javax.annotation.concurrent.Immutable

/**
 * Represents a day on the week calendar.
 *
 * @param date the date for this day.
 * @param position the [WeekDayPosition] for this day.
 */
@Immutable
data class WeekDay(val date: LocalDate, val position: WeekDayPosition) : Serializable
