package com.natiqhaciyef.domain.worker

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.natiqhaciyef.common.constants.EMPTY_STRING
import com.natiqhaciyef.domain.worker.config.sendNotification

class NotificationWorker(val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    companion object {
        var title = EMPTY_STRING
        var description = EMPTY_STRING
        var activityCompat: ComponentActivity? = null
    }

    override fun doWork(): Result {
        sendNotification(
            context = context,
            title = title,
            desc = description,
            activityCompat = activityCompat
        )

        return Result.success()
    }
}