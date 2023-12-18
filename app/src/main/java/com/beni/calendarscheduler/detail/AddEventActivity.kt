package com.beni.calendarscheduler.detail

import android.accounts.Account
import android.accounts.AccountManager
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.beni.calendarscheduler.R
import com.beni.calendarscheduler.databinding.ActivityAddEventBinding
import com.beni.core.util.ConstantFunction.changeFormat
import com.beni.core.util.ConstantFunction.setInputError
import com.beni.core.util.ConstantVariable.TAG
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.DateTime
import com.google.api.services.calendar.CalendarScopes
import com.google.api.services.calendar.model.Event
import com.google.api.services.calendar.model.EventDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class AddEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEventBinding
    private var startDate: Calendar = Calendar.getInstance()
    private var endDate: Calendar = Calendar.getInstance().apply {
        add(Calendar.HOUR_OF_DAY, 1)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            setDateAndTime()
            hideErrorInput()

            tvStartDate.setOnClickListener {
                showDateDialog(START)
            }
            tvEndDate.setOnClickListener {
                showDateDialog(END)
            }
            tvStartTime.setOnClickListener {
                showTimeDialog(START)
            }
            tvEndTime.setOnClickListener {
                showTimeDialog(END)
            }
            icClose.setOnClickListener {
                finish()
            }

            btnAdd.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    getUserInput()
                }
            }
        }
    }

    private fun hideErrorInput() {
        binding.apply {
            etTitle.doAfterTextChanged { ilTitle.isErrorEnabled = false }
            etDesc.doAfterTextChanged { ilDesc.isErrorEnabled = false }
        }
    }

    private fun getGoogleAccountCredential(activity: Activity): GoogleAccountCredential {
        val scopes = listOf(CalendarScopes.CALENDAR)

        val credential = GoogleAccountCredential.usingOAuth2(activity, scopes)
        credential.selectedAccount = getAccount(activity)

        return credential
    }

    private fun getAccount(context: Context): Account? {
        val accountManager = AccountManager.get(context)
        val accounts = accountManager.getAccountsByType("com.google")

        return if (accounts.isNotEmpty()) {
            accounts[0] // Return the first Google account found
        } else {
            null
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getUserInput() {
        binding.apply {
            val title = etTitle.text.toString()
            val desc = etDesc.text.toString()
            if (validateInput(title, desc)) {
                inputEventData(title, desc)
            }
        }
    }

    private fun inputEventData(title: String, desc: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val credential = getGoogleAccountCredential(this@AddEventActivity)
                val calendarService = com.google.api.services.calendar.Calendar.Builder(
                    AndroidHttp.newCompatibleTransport(),
                    JacksonFactory.getDefaultInstance(),
                    credential
                )
                    .setApplicationName(getString(R.string.app_name))
                    .build()

                val event = Event()
                    .setSummary(title)
                    .setDescription(desc)

                val startDateTime = EventDateTime()
                    .setDateTime(DateTime(startDate.time))
                    .setTimeZone(startDate.timeZone.id)

                val endDateTime = EventDateTime()
                    .setDateTime(DateTime(endDate.time))
                    .setTimeZone(startDate.timeZone.id)


                event.start = startDateTime
                event.end = endDateTime

                val calendarId = "primary"

                calendarService.events().insert(calendarId, event).execute()
                Intent().apply {
                    setResult(RESULT_INPUT_EVENT, this)
                    finish()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Log.d(TAG, "inputEventData: ${e.message}")
                    Toast.makeText(this@AddEventActivity, e.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun validateInput(title: String, desc: String): Boolean {
        var isInputEmpty = true
        binding.apply {
            if (title.isEmpty()) {
                isInputEmpty = ilTitle.setInputError(getString(R.string.must_not_empty))
            }
            if (desc.isEmpty()) {
                isInputEmpty = ilDesc.setInputError(getString(R.string.must_not_empty))
            }
        }
        return isInputEmpty
    }

    private fun setDateAndTime() {
        binding.apply {
            tvStartDate.text = startDate.changeFormat("EEE, dd MMM yyyy")
            tvEndDate.text = endDate.changeFormat("EEE, dd MMM yyyy")
            tvStartTime.text = startDate.changeFormat("HH:mm")
            tvEndTime.text = endDate.changeFormat("HH:mm")
        }
    }

    private fun showTimeDialog(timeStatus: String) {
        val calendar = if (timeStatus == START) startDate else endDate
        val time = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            setDateAndTime()
        }
        TimePickerDialog(
            this,
            time,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun showDateDialog(timeStatus: String) {
        val calendar = if (timeStatus == START) startDate else endDate
        val date = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            setDateAndTime()
        }
        DatePickerDialog(
            this,
            date,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
        ).show()
    }

    companion object {
        const val START = "START"
        const val END = "END"
        const val RESULT_INPUT_EVENT = 123
    }
}