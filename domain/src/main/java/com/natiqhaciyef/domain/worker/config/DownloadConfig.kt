package com.natiqhaciyef.domain.worker.config

import android.content.Context
import android.net.Uri
import android.os.Environment
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
import java.util.UUID



fun getSavedFileUri(
    fileName: String,
    fileType: String,
    fileUrl: String,
    context: Context
): Uri? {
    val mimeType = getIntentFileType(fileType) // different types of files will have different mime type

    if (mimeType.isEmpty()) return null

        val target = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            fileName
        )
        context.contentResolver.openInputStream(fileUrl.toUri()).use { input ->
            FileOutputStream(target).use { output ->
                input?.copyTo(output)
            }
        }
        return target.toUri()
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
        putString(FileDownloadWorker.FileParams.KEY_FILE_NAME, "${file.title} (${UUID.randomUUID()})")
        putString(FileDownloadWorker.FileParams.KEY_FILE_URL, file.url.toString())
        putString(FileDownloadWorker.FileParams.KEY_FILE_TYPE, file.type.uppercase())
    }


    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresStorageNotLow(true)
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

                    else -> {}
                }
            }
        }
}
