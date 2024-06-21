package com.natiqhaciyef.domain.worker

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.natiqhaciyef.common.R
import com.natiqhaciyef.common.constants.EMPTY_STRING
import com.natiqhaciyef.common.constants.ONE
import com.natiqhaciyef.common.constants.ZERO
import com.natiqhaciyef.domain.worker.config.getSavedFileUri

class FileDownloadWorker(
    private val context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {
    val downloadingFile = "Downloading your file..."

    object FileParams {
        const val KEY_FILE_URL = "key_file_url"
        const val KEY_FILE_TYPE = "key_file_type"
        const val KEY_FILE_NAME = "key_file_name"
        const val KEY_FILE_URI = "key_file_uri"
    }

    object NotificationConstants {
        const val CHANNEL_NAME = "download_file_worker_demo_channel"
        const val CHANNEL_DESCRIPTION = "download_file_worker_demo_description"
        const val CHANNEL_ID = "download_file_worker_demo_channel_123456"
        const val NOTIFICATION_ID = ONE
    }


    override suspend fun doWork(): Result {
        val fileUrl = inputData.getString(FileParams.KEY_FILE_URL) ?: EMPTY_STRING
        val fileName = inputData.getString(FileParams.KEY_FILE_NAME) ?: EMPTY_STRING
        val fileType = inputData.getString(FileParams.KEY_FILE_TYPE) ?: EMPTY_STRING

        Log.d("TAG", "doWork: $fileUrl | $fileName | $fileType")


        if (fileName.isEmpty()
            || fileType.isEmpty()
            || fileUrl.isEmpty()
        ) {
            Result.failure()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val name = NotificationConstants.CHANNEL_NAME
            val description = NotificationConstants.CHANNEL_DESCRIPTION
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(NotificationConstants.CHANNEL_ID, name, importance)
            channel.description = description

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

            notificationManager?.createNotificationChannel(channel)

        }

        val builder = NotificationCompat.Builder(context, NotificationConstants.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(downloadingFile)
            .setOngoing(true)
            .setProgress(ZERO, ZERO, true)



        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return Result.failure()
        }
        NotificationManagerCompat.from(context)
            .notify(NotificationConstants.NOTIFICATION_ID, builder.build())

        val uri = getSavedFileUri(
            fileName = "$fileName.${fileType.lowercase()}",
            fileType = fileType,
            fileUrl = fileUrl,
            context = context
        )

        NotificationManagerCompat.from(context).cancel(NotificationConstants.NOTIFICATION_ID)
        return if (uri != null) {
            Result.success(workDataOf(FileParams.KEY_FILE_URI to uri.toString()))
        } else {
            Result.failure()
        }
    }
}