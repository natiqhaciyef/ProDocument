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
import com.natiqhaciyef.common.constants.EMPTY_STRING
import com.natiqhaciyef.common.constants.FIVE
import com.natiqhaciyef.common.constants.ONE
import com.natiqhaciyef.common.constants.THREE
import com.natiqhaciyef.common.constants.TWENTY
import com.natiqhaciyef.common.constants.TWO
import com.natiqhaciyef.common.constants.ZERO
import com.natiqhaciyef.common.helpers.getNow
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.model.FileTypes
import com.natiqhaciyef.core.model.FileTypes.ALL_FILES
import com.natiqhaciyef.prodocument.ui.util.DefaultImplModels
import com.shockwave.pdfium.PdfDocument
import java.util.UUID


object FileManager {
    private fun getDefaultMockFile() = DefaultImplModels.mappedMaterialModel

    private const val TAG = "PDF LOADER TAG"
    private const val CONTENT_TITLE = "content://"
    private const val CONTENT_URI = "${CONTENT_TITLE}com.android.externalstorage.documents/"
    private var pageNumber = ZERO
    private var pageTotalCount = ZERO
    private var pagePositionOffset = ZERO.toFloat()
    private var pdfMetadata: PdfDocument.Meta? = null
    private var TITLE_EQUAL = "title = "
    private var AUTHOR_EQUAL = "author = "
    private var SUBJECT_EQUAL = "subject = "
    private var KEYWORD_EQUAL = "keywords = "
    private var CREATOR_EQUAL = "creator = "
    private var PRODUCER_EQUAL = "producer = "
    private var CREATION_DATE_EQUAL = "creationDate = "
    private var MOD_DATE_EQUAL = "modDate = "

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
                    image = uri.toString().removePrefix(CONTENT_TITLE)
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
        material.title = title ?: EMPTY_STRING
        material.description = description
        material.image = image ?: EMPTY_STRING
        material.createdDate = getNow()
        material.type = type ?: FileTypes.PDF

        return material.copy()
    }

    fun getFile(fileRequestLauncher: ActivityResultLauncher<Intent>) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = ALL_FILES
            val uri = Uri.parse(CONTENT_URI)
            putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri)
        }
        fileRequestLauncher.launch(intent)
    }

    fun addDrawListener(ctx: Context) =
        OnDrawListener { canvas, pageWidth, pageHeight, _ ->
            val padding = TWENTY.toFloat()
            val rect = RectF(
                padding,
                padding,
                pageWidth - padding,
                pageHeight - padding
            )
            val paint = Paint().apply {
                color = ctx.getColor(R.color.primary_900)
                style = Paint.Style.STROKE
                strokeWidth = FIVE.toFloat()
            }
            canvas.drawRect(rect, paint)
        }

    fun addLoadCompleteListener(pdfView: PDFView) = OnLoadCompleteListener {
        val meta: PdfDocument.Meta = pdfView.documentMeta
        Log.e(TAG, TITLE_EQUAL + meta.title)
        Log.e(TAG, AUTHOR_EQUAL + meta.author)
        Log.e(TAG,  SUBJECT_EQUAL + meta.subject)
        Log.e(TAG,  KEYWORD_EQUAL + meta.keywords)
        Log.e(TAG, CREATOR_EQUAL + meta.creator)
        Log.e(TAG, PRODUCER_EQUAL + meta.producer)
        Log.e(TAG, CREATION_DATE_EQUAL + meta.creationDate)
        Log.e(TAG, MOD_DATE_EQUAL + meta.modDate)

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
            .pages(ZERO, TWO, ONE, THREE, THREE, THREE) // all pages are displayed by default
            .enableSwipe(true) // allows to block changing pages using swipe
            .swipeHorizontal(false)
            .enableDoubletap(true)
            .defaultPage(ZERO)
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
