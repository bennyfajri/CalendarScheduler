package com.beni.core.data.local

import com.beni.core.data.local.preferences.SampleUser
import com.beni.core.data.local.preferences.UserPreferences
import com.beni.core.data.local.room.SampleDao
import com.beni.core.data.local.room.SampleEntitiy
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val pref: UserPreferences,
    private val room: SampleDao
) {
    fun getUserData() = pref.getUser()

    suspend fun saveUserData(sampleUser: SampleUser) = pref.saveUser(sampleUser)

    suspend fun deleteUserData() = pref.deleteUser()

    suspend fun insertMultipleSampleData(listEntity: List<SampleEntitiy>) = room.insertMultipleSampleData(listEntity)

    suspend fun updateSampleData(sampleEntitiy: SampleEntitiy) = room.updateSampleData(sampleEntitiy)

    fun getAllSampleData(): Flow<List<SampleEntitiy>>  = room.getAllSampleData()

    fun getDataById(id: Int): Flow<SampleEntitiy> = room.getDataById(id)
    suspend fun removeAllItem() = room.removeAllItem()
}