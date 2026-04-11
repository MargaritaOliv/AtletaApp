package com.margaritaolivera.atleta.core.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.margaritaolivera.atleta.core.database.AtletaDao
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val atletaDao: AtletaDao
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        return try {
            val unsynced = atletaDao.getUnsyncedWorkouts()
            unsynced.forEach { workout ->
                atletaDao.markWorkoutAsSynced(workout.id)
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}