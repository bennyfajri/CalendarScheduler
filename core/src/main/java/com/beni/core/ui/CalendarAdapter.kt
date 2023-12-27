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
import com.beni.core.util.ConstantFunction.dpToInt

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
//                        val itemBinding = ItemEventBinding.inflate(
//                            LayoutInflater.from(context),
//                            binding.root,
//                            false
//                        ).apply {
//                            tvEvent.text = data.days[0]
//                        }
//                        llWeek.addView(itemBinding.root)

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
                            textSize =4.dpToInt(context)
                            setTextColor(ContextCompat.getColor(context, R.color.main_text_color))
                            layoutParams = mLayoutParams
                            typeface = Typeface.createFromAsset(context.assets, "nunito.ttf")
                        }
                        llWeek.addView(tvWeek)
                    }
//                    tvWeek1.text = data.days[0]
//                    tvWeek2.text = data.days[1]
//                    tvWeek3.text = data.days[2]
//                    tvWeek4.text = data.days[3]
//
//                    if (data.days.size == 5) {
//                        tvWeek5.text = data.days[4]
//                        tvWeek5.visibility = View.VISIBLE
//                    } else {
//                        tvWeek5.visibility = View.GONE
//                    }
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