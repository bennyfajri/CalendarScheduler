package com.beni.core.util

import androidx.datastore.preferences.core.stringPreferencesKey
import com.beni.core.data.local.preferences.SampleUser

object ConstantVariable {
    const val TAG = "Response::::::::::"
    const val PREF_NAME = "userPreference"
    const val ENTITY_NAME = "sampleEntity"

    val KEY_FULL_NAME = stringPreferencesKey("fullName")
    val KEY_GENDER = stringPreferencesKey("gender")
    val KEY_EMAIL = stringPreferencesKey("email")
    val KEY_TOKEN = stringPreferencesKey("token")

    const val REQUEST_ACCOUNT_PICKER = 1000
    const val REQUEST_AUTHORIZATION = 1001
    const val REQUEST_GOOGLE_PLAY_SERVICES = 1002
    const val REQUEST_PERMISSION_GET_ACCOUNTS = 1003
    const val PREF_ACCOUNT_NAME = "getCalendarEvent"

    val SampleUser.getToken get() = "Bearer ${this.token}"
}