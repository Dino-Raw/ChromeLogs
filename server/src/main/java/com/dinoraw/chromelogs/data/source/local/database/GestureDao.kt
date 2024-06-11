package com.dinoraw.chromelogs.data.source.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dinoraw.chromelogs.domain.model.ClientGestureData
import kotlinx.coroutines.flow.Flow

@Dao
interface GestureDao {
    @Insert
    fun insert(gestureData: ClientGestureData)

    @Query("SELECT * FROM ClientGestureData")
    fun getAll(): Flow<List<ClientGestureData>>

    @Query("DELETE FROM ClientGestureData")
    fun deleteAll()
}