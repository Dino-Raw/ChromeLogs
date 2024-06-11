package com.dinoraw.chromelogs.data.source.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dinoraw.chromelogs.domain.model.ClientGestureData

@Database(
    entities = [ClientGestureData::class],
    version = 1
)
abstract class GestureDataBase: RoomDatabase() {
    abstract fun gestureDao(): GestureDao
}