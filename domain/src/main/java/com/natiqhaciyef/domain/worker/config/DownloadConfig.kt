package com.natiqhaciyef.domain.worker.config

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.net.toUri
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.natiqhaciyef.common.constants.DOWNLOAD_FAILED
import com.natiqhaciyef.common.constants.DOWNLOAD_SUCCEED
import com.natiqhaciyef.common.constants.EMPTY_STRING
import com.natiqhaciyef.common.constants.SOMETHING_WENT_WRONG
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.model.FileTypes.DOCX
import com.natiqhaciyef.core.model.FileTypes.URL
import com.natiqhaciyef.core.model.FileTypes.JPEG
import com.natiqhaciyef.core.model.FileTypes.MP4
import com.natiqhaciyef.core.model.FileTypes.PDF
import com.natiqhaciyef.core.model.FileTypes.PNG
import com.natiqhaciyef.domain.worker.FileDownloadWorker
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.util.UUID



fun getSavedFileUri(
    fileName: String,
    fileType: String,
    fileUrl: String,
    context: Context
): Uri? {
    val mimeType = getIntentFileType(fileType) // different types of files will have different mime type
    val downloadTag = "Download"

    if (mimeType.isEmpty()) return null

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            put(MediaStore.MediaColumns.RELATIVE_PATH, downloadTag)
        }

        val resolver = context.contentResolver

        val uri = resolver.insert(
            MediaStore.Downloads.EXTERNAL_CONTENT_URI,
            contentValues
        )

        return if (uri != null) {
            URL(fileUrl).openStream().use { input ->
                resolver.openOutputStream(uri).use { output ->
                    input.copyTo(output!!, DEFAULT_BUFFER_SIZE)
                }
            }
            uri
        } else {
            null
        }

    } else {

        val target = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            fileName
        )
        URL(fileUrl).openStream().use { input ->
            FileOutputStream(target).use { output ->
                input.copyTo(output)
            }
        }

        return target.toUri()
    }
}

fun getIntentFileType(type: String) = when (type) {
    URL -> "text/plain"
    PDF -> "application/pdf"
    DOCX -> "application/docx"
    PNG -> "image/png"
    JPEG -> "image/jpeg"
    MP4 -> "video/mp4"
    else -> "application/docx"
}

fun startDownloadingFile(
    file: MappedMaterialModel,
    context: Context,
    success: (String) -> Unit = {},
    failed: (String) -> Unit = { },
    running: () -> Unit = {},
) {
    val data = Data.Builder()
    val workManager = WorkManager.getInstance(context)

    data.apply {
        putString(
            FileDownloadWorker.FileParams.KEY_FILE_NAME,
            "${file.title} (${UUID.randomUUID()})"
        )
        putString(FileDownloadWorker.FileParams.KEY_FILE_URL, file.url.toString())
        putString(FileDownloadWorker.FileParams.KEY_FILE_TYPE, file.type)
    }


    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
//        .setRequiresStorageNotLow(true)
        .setRequiresBatteryNotLow(true)
        .build()

    val fileDownloadWorker = OneTimeWorkRequestBuilder<FileDownloadWorker>()
        .setConstraints(constraints)
        .setInputData(data.build())
        .build()

    workManager.enqueueUniqueWork(
        "oneFileDownloadWork_${System.currentTimeMillis()}",
        ExistingWorkPolicy.KEEP,
        fileDownloadWorker
    )


    workManager.getWorkInfoByIdLiveData(fileDownloadWorker.id)
        .observeForever { info ->
            info?.let {
                when (it.state) {
                    WorkInfo.State.SUCCEEDED -> {
                        success(
//                            it.outputData.getString(FileDownloadWorker.FileParams.KEY_FILE_URI)
//                                ?: EMPTY_STRING
                            DOWNLOAD_SUCCEED
                        )
                    }

                    WorkInfo.State.FAILED -> {
                        failed(DOWNLOAD_FAILED)
                    }

                    WorkInfo.State.RUNNING -> {
                        running()
                    }

                    else -> {
                        failed(SOMETHING_WENT_WRONG)
                    }
                }
            }
        }
}
