package com.beni.core.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.beni.core.data.local.models.MCalendar
import com.beni.core.databinding.ItemCalendarBinding

class CalendarAdapter: ListAdapter<MCalendar, CalendarAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCalendarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemCalendarBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: MCalendar) {
            binding.apply {
                tvMonthTitle.text = data.title
                ivMonthImg.setImageResource(data.img)

                if (data.days.isNotEmpty()) {
                    tvWeek1.text = data.days[0]
                    tvWeek2.text = data.days[1]
                    tvWeek3.text = data.days[2]
                    tvWeek4.text = data.days[3]

                    if (data.days.size == 5) {
                        tvWeek5.text = data.days[4]
                        tvWeek5.visibility = View.VISIBLE
                    } else {
                        tvWeek5.visibility = View.GONE
                    }
                }
            }
        }
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