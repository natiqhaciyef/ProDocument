package com.natiqhaciyef.prodocument.ui.manager

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.RectF
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.OpenableColumns
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.activity.result.ActivityResultLauncher
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.listener.OnDrawListener
import com.github.barteksc.pdfviewer.listener.OnErrorListener
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.listener.OnPageScrollListener
import com.natiqhaciyef.common.R
import com.natiqhaciyef.common.helpers.getNow
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.model.FileTypes
import com.natiqhaciyef.prodocument.ui.util.DefaultImplModels
import com.shockwave.pdfium.PdfDocument
import java.util.UUID


object FileManager {
    private fun getDefaultMockFile() = DefaultImplModels.mappedMaterialModel

    private const val TAG = "PDF LOADER TAG"
    private var pageNumber = 0
    private var pageTotalCount = 0
    private var pagePositionOffset = 0.0f
    private var pdfMetadata: PdfDocument.Meta? = null

    @SuppressLint("Range")
    fun readAndCreateFile(
        activity: Activity, uri: Uri,
        action: (MappedMaterialModel) -> Unit = {}
    ) {
        val cursor = activity
            .contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val displayName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                val fileType = MimeTypeMap.getSingleton()
                    .getExtensionFromMimeType(activity.contentResolver.getType(uri))
                val file = createFileObject(
                    uri = uri,
                    title = displayName,
                    type = fileType,
                    image = uri.toString().removePrefix("content://")
                )

                action(file)
            }
        }
    }

    private fun createFileObject(
        uri: Uri,
        title: String? = null,
        description: String? = null,
        image: String? = null,
        type: String? = null
    ): MappedMaterialModel {
        val material = getDefaultMockFile()
        material.id = "${UUID.randomUUID()}"
        material.url = uri
        material.title = title ?: ""
        material.description = description
        material.image = image ?: ""
        material.createdDate = getNow()
        material.type = type ?: FileTypes.PDF

        return material.copy()
    }

    fun getFile(fileRequestLauncher: ActivityResultLauncher<Intent>) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
            val uri = Uri.parse("content://com.android.externalstorage.documents/")
            putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri)
        }
        fileRequestLauncher.launch(intent)
    }

    fun addDrawListener(ctx: Context) =
        OnDrawListener { canvas, pageWidth, pageHeight, _ ->
            val padding = 20f
            val rect = RectF(
                padding,
                padding,
                pageWidth - padding,
                pageHeight - padding
            )
            val paint = Paint().apply {
                color = ctx.getColor(R.color.primary_900)
                style = Paint.Style.STROKE
                strokeWidth = 5f
            }
            canvas.drawRect(rect, paint)
        }

    fun addLoadCompleteListener(pdfView: PDFView) = OnLoadCompleteListener {
        val meta: PdfDocument.Meta = pdfView.documentMeta
        Log.e(TAG, "title = " + meta.title)
        Log.e(TAG, "author = " + meta.author)
        Log.e(TAG, "subject = " + meta.subject)
        Log.e(TAG, "keywords = " + meta.keywords)
        Log.e(TAG, "creator = " + meta.creator)
        Log.e(TAG, "producer = " + meta.producer)
        Log.e(TAG, "creationDate = " + meta.creationDate)
        Log.e(TAG, "modDate = " + meta.modDate)

        pdfMetadata = pdfView.documentMeta
    }

    fun addOnPageChangeListener() = OnPageChangeListener { page, pageCount ->
        pageNumber = page
        pageTotalCount = pageCount
    }

    fun addOnPageScrollListener() = OnPageScrollListener { page, positionOffset ->
        pagePositionOffset = positionOffset
        pageNumber = page
    }

    fun addOnErrorListener() = OnErrorListener {
        Log.e(TAG, "${it.message}")
    }

    fun getPdfName(title: String) =
        String.format("%s %s / %s", title, pageNumber + 1, pageTotalCount)

    fun PDFView.createSafePdfUriLoader(uri: Uri?) = if (uri != null)
        this.fromUri(uri)
            .pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
            .enableSwipe(true) // allows to block changing pages using swipe
            .swipeHorizontal(false)
            .enableDoubletap(true)
            .defaultPage(0)
//            .onDraw(addDrawListener(ctx))
            .onLoad(addLoadCompleteListener(this)) // called after document is loaded and starts to be rendered
            .onPageChange(addOnPageChangeListener())
            .onPageScroll(addOnPageScrollListener())
            .onError(addOnErrorListener())
            .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
            .password(null)
            .scrollHandle(null)
            .load()
    else
        null
}
