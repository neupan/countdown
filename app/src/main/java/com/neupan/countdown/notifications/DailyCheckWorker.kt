package com.neupan.countdown.notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.neupan.countdown.data.AppDatabase
import com.neupan.countdown.utils.DateCalculator
import kotlinx.coroutines.flow.first

class DailyCheckWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    
    override suspend fun doWork(): Result {
        return try {
            val database = AppDatabase.getDatabase(applicationContext)
            val memorialDays = database.memorialDayDao().getAllMemorialDays().first()
            
            // 创建通知通道
            NotificationHelper.createNotificationChannel(applicationContext)
            
            // 检查每个纪念日是否需要提醒
            memorialDays.forEach { memorialDay ->
                if (DateCalculator.isInReminderRange(memorialDay)) {
                    val daysRemaining = DateCalculator.getDaysRemaining(memorialDay)
                    NotificationHelper.showReminderNotification(
                        applicationContext,
                        memorialDay.name,
                        daysRemaining
                    )
                }
            }
            
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}

