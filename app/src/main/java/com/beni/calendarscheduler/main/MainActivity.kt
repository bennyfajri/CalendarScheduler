package com.beni.calendarscheduler.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beni.calendarscheduler.R
import com.beni.calendarscheduler.databinding.ActivityMainBinding
import com.beni.calendarscheduler.detail.AddEventActivity
import com.beni.core.ui.CalendarAdapter
import com.beni.core.util.ConstantFunction
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.api.services.calendar.CalendarScopes
import com.prolificinteractive.materialcalendarview.CalendarDay
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var topPos = 0
    private lateinit var binding: ActivityMainBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleSignInAccount: GoogleSignInAccount
    private lateinit var mAdapter: CalendarAdapter
    private val viewModel: MainViewModel by viewModels()
    private lateinit var scrollListener: RecyclerView.OnScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAdapter = CalendarAdapter()
        val layoutManager = binding.rvEvents.layoutManager as LinearLayoutManager
//        initializeUserAccount()
        goToAddEvent()

        scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val currentPos = layoutManager.findFirstVisibleItemPosition()
                if (currentPos != topPos) {
                    val newDay = ConstantFunction.getCalendarDay(currentPos)
                    binding.tvDatePickerText.text = ConstantFunction.getTitleText(newDay)
                    topPos = currentPos
                }
            }
        }

        viewModel.isLoading.observe(this@MainActivity) {
            binding.progressBar.isVisible = it
        }
        viewModel.dateList.observe(this@MainActivity) {
            mAdapter.submitList(it)
            topPos = layoutManager.findFirstVisibleItemPosition()
            binding.apply {
                rvEvents.adapter = mAdapter
                rvEvents.addOnScrollListener(scrollListener)
                tvDatePickerText.text = ConstantFunction.getTitleText(CalendarDay.today())
                layoutManager.scrollToPositionWithOffset(ConstantFunction.getPosition(CalendarDay.today().year, CalendarDay.today().month), 0)

            }
        }

        binding.apply {
            ivCurrentDate.setOnClickListener {
                tvDatePickerText.text = ConstantFunction.getTitleText(CalendarDay.today())
                layoutManager.scrollToPositionWithOffset(ConstantFunction.getPosition(CalendarDay.today().year, CalendarDay.today().month), 0)
            }
        }

    }

    private fun goToAddEvent() {
        binding.fabAdd.setOnClickListener {
            launcherInputEvent.launch(Intent(this, AddEventActivity::class.java))
        }
    }

    private val launcherInputEvent = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AddEventActivity.RESULT_INPUT_EVENT) {
            initializeUserAccount()
        }
    }

    private fun initializeUserAccount() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope(CalendarScopes.CALENDAR_READONLY))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        signIn()
    }

    private val launcherLoginGoogle = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignInResult(task)
        } else {
            MaterialAlertDialogBuilder(this@MainActivity)
                .setMessage(getString(R.string.must_login_first))
                .setTitle(getString(R.string.attention))
                .setNegativeButton(getString(R.string.close_app)) { dialog, _ ->
                    dialog.dismiss()
                    finishAffinity()
                }
                .setPositiveButton(getString(R.string.login)) { dialog, _ ->
                    dialog.dismiss()
                    signIn()
                }.show()
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
//            fetchCalendarEvents(account!!)
        } catch (e: ApiException) {
            e.printStackTrace()
        }
    }

//    private fun fetchCalendarEvents(account: GoogleSignInAccount) {
//        lifecycleScope.launch(Dispatchers.IO) {
//            try {
//                val transport = AndroidHttp.newCompatibleTransport()
//                val jsonFactory: JsonFactory = JacksonFactory.getDefaultInstance()
//                val credential = GoogleAccountCredential.usingOAuth2(
//                    this@MainActivity,
//                    listOf(CalendarScopes.CALENDAR_READONLY)
//                )
//                credential.selectedAccount = account.account
//
//                val service = Calendar.Builder(transport, jsonFactory, credential)
//                    .setApplicationName(getString(R.string.app_name))
//                    .build()
//
//                val events = service.events()
//                    .list("primary") // 'primary' refers to the user's primary calendar
////                    .setMaxResults(10) // Number of events to retrieve
//                    .execute()
//
//                // Handle retrieved events on the main thread
//                launch(Dispatchers.Main) {
//                    val itemEvents = events.items
//                    Log.d(TAG, "fetchCalendarEvents: $itemEvents")
//                    mAdapter.submitList(itemEvents)
//                    setupRecyclerView()
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }

    private fun setupRecyclerView() {
        binding.rvEvents.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
        }
    }


    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        launcherLoginGoogle.launch(signInIntent)
    }
}