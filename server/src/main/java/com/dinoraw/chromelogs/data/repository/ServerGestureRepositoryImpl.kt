package com.dinoraw.chromelogs.data.repository

import com.dinoraw.chromelogs.data.source.local.database.GestureDao
import com.dinoraw.chromelogs.domain.model.ClientGestureData
import com.dinoraw.chromelogs.domain.repository.ServerGestureRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ServerGestureRepositoryImpl @Inject constructor(
    private val gestureDao: GestureDao
) : ServerGestureRepository {
    override fun setGesture(gestureData: ClientGestureData): Result<Unit> = runCatching {
        gestureDao.insert(gestureData)
    }

    override fun getGesture(): Flow<Result<List<ClientGestureData>>> = gestureDao.getAll().map {
        Result.success(it)
    }

    override fun deleteGesture(): Result<Unit> = runCatching {
        gestureDao.deleteAll()
    }
}