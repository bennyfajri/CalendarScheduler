package com.beni.core.ui

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout.LayoutParams
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.beni.core.R
import com.beni.core.data.local.models.MCalendar
import com.beni.core.databinding.ItemCalendarBinding
import com.beni.core.databinding.ItemEventBinding
import com.beni.core.util.ConstantFunction.dpToInt
import com.google.api.client.util.DateTime
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CalendarAdapter : ListAdapter<MCalendar, CalendarAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCalendarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemCalendarBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: MCalendar) {
            binding.apply {
                val context = itemView.context
                tvMonthTitle.text = data.title
                ivMonthImg.setImageResource(data.img)

                if (data.days.isNotEmpty()) {
                    llWeek.removeAllViews()
                    for (i in data.days.indices) {
                        val mLayoutParams = LayoutParams(
                            LayoutParams.MATCH_PARENT,
                            LayoutParams.WRAP_CONTENT
                        )
                        val tvWeek = TextView(itemView.context).apply {
                            setPadding(
                                48.dpToInt(context).toInt(),
                                6.dpToInt(context).toInt(),
                                0,
                                6.dpToInt(context).toInt()
                            )
                            text = data.days[i]
                            textSize = 4.dpToInt(context)
                            setTextColor(ContextCompat.getColor(context, R.color.main_text_color))
                            layoutParams = mLayoutParams
                            typeface = Typeface.createFromAsset(context.assets, "nunito.ttf")
                        }
                        llWeek.addView(tvWeek)
                        data.events?.let {
                            if (it.isNotEmpty()) {
                                for (j in it.indices) {
                                    val event = data.events!![j]
                                    val dateRange = data.days[i].split("―").map { date ->
                                        date.trim()
                                    }
                                    val year = dateRange[1].split(",")[1]
                                    val dateTime = event.start.dateTime
                                    val yesterdayDate = if (j > 0) {
                                        data.events!![j - 1].start.dateTime.formatDate("dd, EEE")
                                    } else {
                                        null
                                    }

                                    val endDateString = dateRange[1]
                                    val startDateString = if (dateRange[0].length < 3) {
                                        "${dateRange[0]}-${endDateString.split("-")[1].split(",")[0]}"
                                    } else {
                                        dateRange[0]
                                    }
                                    val dateFormat = SimpleDateFormat(
                                        "d-MMM, yyyy",
                                        Locale.getDefault()
                                    )
                                    val startDate = dateFormat.parse("$startDateString, $year")
                                    val endDate = dateFormat.parse("$endDateString, $year")
                                    val date = dateFormat.parse(
                                        dateTime?.formatDate("d-MMM, yyyy") ?: ""
                                    )

                                    if (date in startDate..endDate) {
                                        val itemBinding = ItemEventBinding.inflate(
                                            LayoutInflater.from(context),
                                            binding.root,
                                            false
                                        ).apply {
                                            val dateTimeFormat = dateTime?.formatDate("dd, EEE")
                                            tvEvent.text = data.events!![j].summary ?: "--"
                                            tvDate.text = if (yesterdayDate != dateTimeFormat) {
                                                dateTime?.formatDate("dd") ?: ""
                                            } else {
                                                ""
                                            }

                                            tvDay.text = if (yesterdayDate != dateTimeFormat) {
                                                dateTime?.formatDate("EEE") ?: ""
                                            } else {
                                                ""
                                            }
                                        }
                                        llWeek.addView(itemBinding.root)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun DateTime.formatDate(format: String): String {
        val df = SimpleDateFormat(format, Locale.getDefault())
        return df.format(Date(this.value))
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<MCalendar> =
            object : DiffUtil.ItemCallback<MCalendar>() {
                override fun areItemsTheSame(
                    oldItem: MCalendar,
                    newItem: MCalendar
                ): Boolean {
                    return oldItem.title == newItem.title
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldItem: MCalendar,
                    newItem: MCalendar
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}