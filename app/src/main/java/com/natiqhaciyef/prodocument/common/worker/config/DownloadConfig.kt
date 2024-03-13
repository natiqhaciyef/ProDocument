package com.natiqhaciyef.prodocument.common.worker.config

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.Images
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.natiqhaciyef.prodocument.BuildConfig
import com.natiqhaciyef.prodocument.common.worker.FileDownloadWorker
import com.natiqhaciyef.prodocument.domain.model.mapped.MappedMaterialModel
import com.natiqhaciyef.prodocument.ui.base.BaseFragment
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.util.UUID


const val PDF = "PDF"
const val DOCX = "DOCX"
const val PNG = "PNG"
const val JPEG = "JPEG"
const val MP4 = "MP4"

fun getSavedFileUri(
    fileName: String,
    fileType: String,
    fileUrl: String,
    context: Context
): Uri? {
    val mimeType =
        getIntentFileType(fileType) // different types of files will have different mime type

    if (mimeType.isEmpty()) return null

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Download")
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
    PDF -> "application/pdf"
    DOCX -> "application/docx"
    PNG -> "image/png"
    JPEG -> "image/jpeg"
    MP4 -> "video/mp4"
    else -> "application/docx"
}

fun startDownloadingFile(
    file: MappedMaterialModel,
    success: (String) -> Unit,
    failed: (String) -> Unit,
    running: () -> Unit,
    context: Context
) {
    val data = Data.Builder()
    val workManager = WorkManager.getInstance(context)

    data.apply {
        putString(
            FileDownloadWorker.FileParams.KEY_FILE_NAME,
            "${file.title} (${UUID.randomUUID()})"
        )
        putString(FileDownloadWorker.FileParams.KEY_FILE_URL, file.url)
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
                            it.outputData.getString(FileDownloadWorker.FileParams.KEY_FILE_URI)
                                ?: ""
                        )
                    }

                    WorkInfo.State.FAILED -> {
                        failed("Downloading failed!")
                    }

                    WorkInfo.State.RUNNING -> {
                        running()
                    }

                    else -> {
                        failed("Something went wrong")
                    }
                }
            }
        }
}

fun BaseFragment.createAndShareFile(
    context: Context,
    fileType: String,
    urls: List<String>,
) = when (fileType) {
    PDF -> {
        val externalPdfUri = FileProvider.getUriForFile(
            context,
            "${BuildConfig.APPLICATION_ID}.provider",
            File(urls[0])
        )

        val shareIntent =
            Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_STREAM, externalPdfUri)
                type = getIntentFileType(PDF)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

        startActivity(Intent.createChooser(shareIntent, "share pdf"))
        listOf(externalPdfUri)
    }

    JPEG, PNG -> {
        val list = mutableListOf<String>()
        for (url in urls) {
            val uri = shareImage(url.toUri())
            list.add(uri.toString())
        }
        list
    }

    else -> {
        listOf()
    }
}

private fun BaseFragment.shareImage(uri: Uri): Uri {
    val sharingIntent = Intent(Intent.ACTION_SEND)
    val screenshotUri = Uri.parse(Images.Media.EXTERNAL_CONTENT_URI.toString() + "/" + uri)

    sharingIntent.setType(getIntentFileType(JPEG))
    sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri)
    startActivity(Intent.createChooser(sharingIntent, "Share image using"))

    return screenshotUri
}
