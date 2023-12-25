package com.beni.core.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.beni.core.databinding.ItemEventBinding
import com.google.api.client.util.DateTime
import com.google.api.services.calendar.model.Event
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class EventsAdapter : ListAdapter<Event, EventsAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemEventBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Event) {
            binding.apply {
                tvDate.text = data.start.dateTime?.formatDate("dd") ?: ""
                tvDay.text = data.start.dateTime?.formatDate("EEE") ?: ""
                tvEvent.text = data.summary
            }
        }
    }

    fun DateTime.formatDate(format: String): String {
        val df = SimpleDateFormat(format, Locale.getDefault())
        return df.format(Date(this.value))
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Event> =
            object : DiffUtil.ItemCallback<Event>() {
                override fun areItemsTheSame(
                    oldItem: Event,
                    newItem: Event
                ): Boolean {
                    return oldItem.recurringEventId == newItem.recurringEventId
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldItem: Event,
                    newItem: Event
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}