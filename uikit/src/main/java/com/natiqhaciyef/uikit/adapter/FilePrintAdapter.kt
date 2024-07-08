package com.natiqhaciyef.uikit.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import com.natiqhaciyef.common.constants.ONE
import com.natiqhaciyef.common.constants.SEVENTY_TWO
import com.natiqhaciyef.common.constants.THOUSAND
import com.natiqhaciyef.common.constants.ZERO
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import java.io.FileOutputStream
import java.io.IOException


class FilePrintAdapter(
    private val context: Context,
    private val materialModel: MappedMaterialModel,
) : PrintDocumentAdapter() {
    private var pdfRenderer: PdfRenderer? = null
    private var pageHeight: Int = ZERO
    private var pageWidth: Int = ZERO
    private var pdfDocument: PdfDocument? = null

    override fun onLayout(
        oldAttributes: PrintAttributes,
        newAttributes: PrintAttributes,
        cancellationSignal: android.os.CancellationSignal,
        callback: LayoutResultCallback,
        extras: Bundle?
    ) {
        pdfDocument = PdfDocument()
        pageHeight = newAttributes.mediaSize!!.heightMils / THOUSAND * SEVENTY_TWO
        pageWidth = newAttributes.mediaSize!!.widthMils / THOUSAND * SEVENTY_TWO

        if (cancellationSignal.isCanceled) {
            callback.onLayoutCancelled()
            return
        }

        val info = PrintDocumentInfo.Builder("${materialModel.title}.${materialModel.type.lowercase()}")
            .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
            .setPageCount(ONE)
            .build()
        callback.onLayoutFinished(info, true)
    }

    override fun onWrite(
        pages: Array<PageRange>,
        destination: ParcelFileDescriptor,
        cancellationSignal: android.os.CancellationSignal,
        callback: WriteResultCallback
    ) {
        val fileDescriptor = context.contentResolver.openFileDescriptor(materialModel.url, "r")
        if (fileDescriptor != null) {
            pdfRenderer = PdfRenderer(fileDescriptor)
        }

        pdfRenderer?.let { renderer ->
            for (i in ZERO until renderer.pageCount) {
                if (containsPage(pages, i)) {
                    val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, i).create()
                    val page = pdfDocument!!.startPage(pageInfo)

                    if (cancellationSignal.isCanceled) {
                        callback.onWriteCancelled()
                        pdfDocument!!.close()
                        pdfDocument = null
                        return
                    }

                    drawPage(renderer, page.canvas, i)
                    pdfDocument!!.finishPage(page)
                }
            }

            try {
                pdfDocument!!.writeTo(FileOutputStream(destination.fileDescriptor))
            } catch (e: IOException) {
                callback.onWriteFailed(e.toString())
                return
            } finally {
                pdfDocument!!.close()
                pdfDocument = null
                renderer.close()
            }

            callback.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
        }
    }

    private fun drawPage(renderer: PdfRenderer, canvas: Canvas, pageIndex: Int) {
        val pdfPage = renderer.openPage(pageIndex)

        val bitmap = Bitmap.createBitmap(
            pdfPage.width, pdfPage.height,
            Bitmap.Config.ARGB_8888
        )
        pdfPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_PRINT)

        canvas.drawBitmap(bitmap, ZERO.toFloat(), ZERO.toFloat(), null)
        pdfPage.close()
    }
    private fun containsPage(pages: Array<PageRange>, pageIndex: Int): Boolean {
        for (pageRange in pages) {
            if (pageIndex in pageRange.start..pageRange.end) {
                return true
            }
        }
        return false
    }
}