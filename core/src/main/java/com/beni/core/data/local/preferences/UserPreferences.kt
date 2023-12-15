package com.beni.core.data.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.beni.core.util.ConstantVariable
import com.beni.core.util.ConstantVariable.KEY_EMAIL
import com.beni.core.util.ConstantVariable.KEY_FULL_NAME
import com.beni.core.util.ConstantVariable.KEY_GENDER
import com.beni.core.util.ConstantVariable.KEY_TOKEN
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.userStore by preferencesDataStore(name = ConstantVariable.PREF_NAME)

class UserPreferences(context: Context) {
    private val userDataStore = context.userStore

    fun getUser(): Flow<SampleUser> {
        return userDataStore.data.map { pref ->
            SampleUser(
                fullName = pref[KEY_FULL_NAME] ?: "",
                gender = pref[KEY_GENDER] ?: "",
                email = pref[KEY_EMAIL] ?: "",
                token = pref[KEY_TOKEN] ?: "",
            )
        }
    }

    suspend fun saveUser(sampleUser: SampleUser) {
        userDataStore.edit { pref ->
            pref[KEY_FULL_NAME] = sampleUser.fullName
            pref[KEY_GENDER] = sampleUser.gender
            pref[KEY_EMAIL] = sampleUser.email
            pref[KEY_TOKEN] = sampleUser.token
        }
    }

    suspend fun deleteUser() {
        userDataStore.edit { pref ->
            pref.clear()
        }
    }
}