package com.natiqhaciyef.prodocument.ui.util

import android.content.Context
import android.graphics.Paint
import android.graphics.RectF
import android.net.Uri
import android.util.Log
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.listener.OnDrawListener
import com.github.barteksc.pdfviewer.listener.OnErrorListener
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.listener.OnPageScrollListener
import com.natiqhaciyef.common.R
import com.shockwave.pdfium.PdfDocument

object PdfReader {
    private const val TAG = "PDF LOADER TAG"
    private var pageNumber = 0
    private var pageTotalCount = 0
    private var pagePositionOffset = 0.0f
    private var pdfMetadata: PdfDocument.Meta? = null

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

    fun PDFView.createDefaultPdfUriLoader(ctx: Context, uri: Uri) =
        this.fromUri(uri)
            .pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
            .enableSwipe(true) // allows to block changing pages using swipe
            .swipeHorizontal(false)
            .enableDoubletap(true)
            .defaultPage(0)
            .onDraw(addDrawListener(ctx))
            .onLoad(addLoadCompleteListener(this)) // called after document is loaded and starts to be rendered
            .onPageChange(addOnPageChangeListener())
            .onPageScroll(addOnPageScrollListener())
            .onError(addOnErrorListener())
            .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
            .password(null)
            .scrollHandle(null)
            .load()
}

