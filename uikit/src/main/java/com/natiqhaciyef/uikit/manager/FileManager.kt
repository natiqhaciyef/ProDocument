package com.natiqhaciyef.uikit.manager

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
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
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
import com.natiqhaciyef.domain.worker.config.getIntentFileType
import com.shockwave.pdfium.PdfDocument
import java.io.File
import java.util.UUID


object FileManager {
    private fun getDefaultMockFile() = MappedMaterialModel(
        id = "$ZERO",
        image = EMPTY_STRING,
        title = EMPTY_STRING,
        description = null,
        createdDate = EMPTY_STRING,
        type = EMPTY_STRING,
        url = EMPTY_STRING.toUri(),
        result = null
    )

    private const val TAG = "PDF LOADER TAG"
    private const val CONTENT_TITLE = "content://"
    private const val CONTENT_URI = "${CONTENT_TITLE}com.android.externalstorage.documents/"
    private var pageNumber = ZERO
    private var pageTotalCount = ZERO
    private var pagePositionOffset = ZERO.toFloat()
    private const val PDF_EXTENSION = ".pdf"
    private const val PNG_EXTENSION = ".png"
    private const val DOCX_EXTENSION = ".docx"
    private const val PROVIDER = "provider"
    private const val SHARE_DATE_USING = "Share data using"
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
        val uri = Uri.parse(CONTENT_URI)
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = ALL_FILES
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


    fun Fragment.createAndShareFile(
        applicationId: String,
        material: MappedMaterialModel,
        isShare: Boolean = true
    ) = when (material.type) {
        FileTypes.URL -> {
            shareFile(applicationId, listOf(material.url), FileTypes.URL, isShare)
        }

        FileTypes.PDF -> {
            shareFile(applicationId, listOf(material.url), FileTypes.PDF, isShare)
        }

        FileTypes.DOCX -> {
            shareFile(applicationId, listOf(material.url), FileTypes.DOCX, isShare)
        }

        FileTypes.JPEG -> {
            shareFile(applicationId, listOf(material.image.toUri(), material.url), FileTypes.JPEG, isShare)
        }

        FileTypes.PNG -> {
            shareFile(applicationId, listOf(material.image.toUri(), material.url), FileTypes.PNG, isShare)
        }

        else -> {
            listOf()
        }
    }

    private fun Fragment.shareFile(
        applicationId: String,
        urls: List<Uri>,
        fileType: String,
        isShare: Boolean = true
    ): List<Uri?> {
        val list = mutableListOf<Uri?>()
        val sharingIntent = Intent(Intent.ACTION_SEND)

        when(fileType){
            FileTypes.URL -> {
                if (urls.isNotEmpty()) {
                    val url = urls[ZERO].toString().replace(PDF_EXTENSION, EMPTY_STRING).toUri()
                    val address = getAddressOfFile(applicationId, requireContext(), url)
                    list.add(address)

                    sharingIntent.apply {
                        type = getIntentFileType(fileType)
                        putExtra(Intent.EXTRA_TEXT, url.toString())
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                }
            }

            FileTypes.PDF -> {
                if (urls.isNotEmpty()) {
                    val externalUri = getAddressOfFile(applicationId, requireContext(), urls[ZERO])
                    if (isShare)
                        sharingIntent.apply {
                            type = getIntentFileType(fileType)
                            putExtra(Intent.EXTRA_STREAM, externalUri)
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                    list.add(externalUri)
                }
            }

            FileTypes.DOCX -> {
                val url = urls[ZERO].toString().replace(PDF_EXTENSION, DOCX_EXTENSION).toUri()
                val address = getAddressOfFile(applicationId, requireContext(), url)
                list.add(address)

                sharingIntent.apply {
                    type = getIntentFileType(fileType)
                    putExtra(Intent.EXTRA_STREAM, url.toString())
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
            }

            FileTypes.PNG, FileTypes.JPEG -> {
                val url = urls[ZERO].toString().replace(PDF_EXTENSION, PNG_EXTENSION).toUri()
                val address = getAddressOfFile(applicationId, requireContext(), url)
                list.add(address)

                sharingIntent.apply {
                    type = getIntentFileType(fileType)
                    putExtra(Intent.EXTRA_STREAM, url.toString())
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
            }

            else -> {
                for (url in urls) {
                    list.add(getAddressOfFile(applicationId, requireContext(), url))
                }

                if (isShare)
                    sharingIntent.apply {
                        type = getIntentFileType(fileType)
                        putExtra(Intent.EXTRA_STREAM, list[ZERO])
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
            }
        }

        startActivity(Intent.createChooser(sharingIntent, SHARE_DATE_USING))
        return list
    }

    fun getAddressOfFile(applicationId: String, context: Context, uri: Uri?) = if (uri != null) {
        FileProvider.getUriForFile(
            context,
            "$applicationId.$PROVIDER",
            File(uri.path.toString())
        )
    }else {
        null
    }
}
