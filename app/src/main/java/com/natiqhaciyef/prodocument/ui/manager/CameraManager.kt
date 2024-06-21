package com.natiqhaciyef.prodocument.ui.manager

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import com.natiqhaciyef.prodocument.BuildConfig
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_JPEG
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_PDF
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.SCANNER_MODE_FULL
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.natiqhaciyef.common.constants.EMPTY_STRING
import com.natiqhaciyef.common.constants.PHOTO_CAPTURED_FAILED
import com.natiqhaciyef.common.constants.PHOTO_CAPTURED_SUCCEED
import com.natiqhaciyef.common.constants.THIRTY
import com.natiqhaciyef.common.constants.ZERO
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.model.FileTypes.DOCX
import com.natiqhaciyef.core.model.FileTypes.JPEG
import com.natiqhaciyef.core.model.FileTypes.PDF
import com.natiqhaciyef.core.model.FileTypes.PNG
import com.natiqhaciyef.core.model.FileTypes.URL
import com.natiqhaciyef.domain.worker.config.getIntentFileType
import java.io.File
import java.util.Locale
import java.util.concurrent.Executors


class CameraManager(
    private val context: Context,
    private val lifecycle: LifecycleOwner,
) {
    private var cameraProvider: ProcessCameraProvider? = null


    // Barcode scanner
    @ExperimentalGetImage
    fun openBarcodeScanner(
        surfaceProvider: PreviewView,
        onSuccess: (String) -> Unit = { }
    ) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        // options
        val options = BarcodeScannerOptions.Builder().setBarcodeFormats(
            Barcode.FORMAT_CODE_128,
            Barcode.FORMAT_CODE_39,
            Barcode.FORMAT_CODE_93,
            Barcode.FORMAT_EAN_8,
            Barcode.FORMAT_EAN_13,
            Barcode.FORMAT_QR_CODE,
            Barcode.FORMAT_UPC_A,
            Barcode.FORMAT_UPC_E,
            Barcode.FORMAT_PDF417
        ).build()
        val scanner = BarcodeScanning.getClient(options)
        val analysisUseCase = ImageAnalysis.Builder()
            .build()

        analysisUseCase.setAnalyzer(
            Executors.newSingleThreadExecutor()
        ) { imageProxy ->
            processBarcodeImageProxy(scanner, imageProxy, onSuccess)
        }


        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(surfaceProvider.surfaceProvider)
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProvider?.unbindAll()
                cameraProvider?.bindToLifecycle(lifecycle, cameraSelector, preview, analysisUseCase)

            } catch (exc: Exception) {
                println(exc)
            }

        }, ContextCompat.getMainExecutor(context))
    }


    @ExperimentalGetImage
    private fun processBarcodeImageProxy(
        barcodeScanner: BarcodeScanner,
        imageProxy: ImageProxy,
        onSuccess: (String) -> Unit
    ) {
        imageProxy.image?.let { image ->
            val inputImage =
                InputImage.fromMediaImage(
                    image,
                    imageProxy.imageInfo.rotationDegrees
                )

            barcodeScanner.process(inputImage)
                .addOnSuccessListener { barcodeList ->
                    val barcode = barcodeList.getOrNull(ZERO)
                    barcode?.rawValue?.let { value ->
                        onSuccess(value)
                    }
                }
                .addOnFailureListener {
                    Log.e(ContentValues.TAG, it.message.orEmpty())
                }.addOnCompleteListener {
                    imageProxy.image?.close()
                    imageProxy.close()
                }
        }
    }


    // Live reader
    @ExperimentalGetImage
    fun openLiveTextRecognizer(
        cameraXPreviewHolder: PreviewView,
        button: View? = null,
        onSuccess: (Any) -> Unit
    ) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        val imageCapture = ImageCapture.Builder().build()

        val analysisUseCase = ImageAnalysis.Builder()
            .build()

        analysisUseCase.setAnalyzer(Executors.newSingleThreadExecutor()) {
            processRecognizerImageProxy(it)
        }

        val name = SimpleDateFormat(FILE_NAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, JPEG)
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, PICTURES_SLASH_CAMERAX_IMAGE)
            }
        }


        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(
                context.contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            .build()


        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            // preview use case
            val preview = Preview.Builder()
                .build()
                .also { it.setSurfaceProvider(cameraXPreviewHolder.surfaceProvider) }


            button?.setOnClickListener {
                imageCapture.takePicture(
                    outputOptions,
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onError(exc: ImageCaptureException) {
                            Log.e(TAG, "$PHOTO_CAPTURED_FAILED: ${exc.message}", exc)
                        }

                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                            val msg = "$PHOTO_CAPTURED_SUCCEED: ${output.savedUri}"
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            Log.d(TAG, msg)
                            output.savedUri?.let(onSuccess)
                        }
                    }
                )
            }

            try {
                cameraProvider?.unbindAll()
                cameraProvider?.bindToLifecycle(
                    lifecycle,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageCapture,
                    analysisUseCase
                )

            } catch (exc: Exception) {
                println(exc)
            }

        }, ContextCompat.getMainExecutor(context))
    }


    @ExperimentalGetImage
    private fun processRecognizerImageProxy(
        imageProxy: ImageProxy,
        onSuccess: (Text) -> Unit = {}
    ) {
        imageProxy.image?.let { image ->
            val inputImage = InputImage.fromMediaImage(image, imageProxy.imageInfo.rotationDegrees)

            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

            recognizer.process(inputImage)
                .addOnSuccessListener { visionText ->
                    processTextBlock(visionText)
                    onSuccess(visionText)
                }
                .addOnFailureListener {
                    Log.e(ContentValues.TAG, it.message.orEmpty())
                }.addOnCompleteListener {
                    imageProxy.image?.close()
                    imageProxy.close()
                }
        }
    }


    // Captured reader

    @ExperimentalGetImage
    fun openCapturedImageTextRecognizer(
        cameraXPreviewHolder: PreviewView,
        button: View? = null,
        onSuccess: (Any) -> Unit
    ) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        val imageCapture = ImageCapture.Builder().build()


        val name = SimpleDateFormat(FILE_NAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, JPEG)
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, PICTURES_SLASH_CAMERAX_IMAGE)
            }
        }


        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(
                context.contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            .build()


        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            // preview use case
            val preview = Preview.Builder()
                .build()
                .also { it.setSurfaceProvider(cameraXPreviewHolder.surfaceProvider) }


            button?.setOnClickListener {
                imageCapture.takePicture(
                    outputOptions,
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onError(exc: ImageCaptureException) {
                            Log.e(TAG, "$PHOTO_CAPTURED_FAILED: ${exc.message}", exc)
                        }

                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                            val msg = "$PHOTO_CAPTURED_SUCCEED: ${output.savedUri}"
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            Log.e(TAG, msg)
                            processRecognizerCapturedImage(output.savedUri)
                            output.savedUri?.let(onSuccess)
                        }
                    }
                )
            }

            try {
                cameraProvider?.unbindAll()
                cameraProvider?.bindToLifecycle(
                    lifecycle,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageCapture,
                )

            } catch (exc: Exception) {
                println(exc)
            }

        }, ContextCompat.getMainExecutor(context))
    }

    @ExperimentalGetImage
    private fun processRecognizerCapturedImage(
        uri: Uri? = null,
        onSuccess: (Text) -> Unit = {}
    ) {
        uri?.let {
            val inputImage = InputImage.fromFilePath(context, uri)

            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

            recognizer.process(inputImage)
                .addOnSuccessListener { visionText ->
                    processTextBlock(visionText)
                    onSuccess(visionText)
                }
                .addOnFailureListener {
                    Log.e(ContentValues.TAG, it.message.orEmpty())
                }
        }
    }


    private fun processTextBlock(result: Text) {
        val resultText = result.text
        for (block in result.textBlocks) {
            val blockText = block.text
            val blockCornerPoints = block.cornerPoints
            val blockFrame = block.boundingBox
            for (line in block.lines) {
                val lineText = line.text
                val lineCornerPoints = line.cornerPoints
                val lineFrame = line.boundingBox
                for (element in line.elements) {
                    val elementText = element.text
                    val elementCornerPoints = element.cornerPoints
                    val elementFrame = element.boundingBox

                    println(elementText)
                }
            }
        }
    }

    companion object {
        val cameraScannerDefaultOptions = GmsDocumentScannerOptions.Builder()
            .setGalleryImportAllowed(true)
            .setPageLimit(THIRTY)
            .setResultFormats(RESULT_FORMAT_PDF, RESULT_FORMAT_JPEG)
            .setScannerMode(SCANNER_MODE_FULL)
            .build()

        fun Fragment.createAndShareFile(
            material: MappedMaterialModel,
            isShare: Boolean = true
        ) = when (material.type) {
            URL -> {
                shareFile(listOf(material.url), URL, isShare)
            }

            PDF -> {
                shareFile(listOf(material.url), PDF, isShare)
            }

            DOCX -> {
                shareFile(listOf(material.url), DOCX, isShare)
            }

            JPEG -> {
                shareFile(listOf(material.image.toUri(), material.url), JPEG, isShare)
            }

            PNG -> {
                shareFile(listOf(material.image.toUri(), material.url), PNG, isShare)
            }

            else -> {
                listOf()
            }
        }

        private fun Fragment.shareFile(
            urls: List<Uri>,
            fileType: String,
            isShare: Boolean = true
        ): List<Uri?> {
            val list = mutableListOf<Uri?>()
            val sharingIntent = Intent(Intent.ACTION_SEND)

            when(fileType){
                URL -> {
                    if (urls.isNotEmpty()) {
                        val url = urls[ZERO].toString().replace(PDF_EXTENSION, EMPTY_STRING).toUri()
                        val address = getAddressOfFile(requireContext(), url)
                        list.add(address)

                        sharingIntent.apply {
                            type = getIntentFileType(fileType)
                            putExtra(Intent.EXTRA_TEXT, url.toString())
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                    }
                }

                PDF -> {
                    if (urls.isNotEmpty()) {
                        val externalUri = getAddressOfFile(requireContext(), urls[ZERO])
                        if (isShare)
                            sharingIntent.apply {
                                type = getIntentFileType(fileType)
                                putExtra(Intent.EXTRA_STREAM, externalUri)
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                        list.add(externalUri)
                    }
                }

                DOCX -> {
                    val url = urls[ZERO].toString().replace(PDF_EXTENSION, DOCX_EXTENSION).toUri()
                    val address = getAddressOfFile(requireContext(), url)
                    list.add(address)

                    sharingIntent.apply {
                        type = getIntentFileType(fileType)
                        putExtra(Intent.EXTRA_STREAM, url.toString())
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                }

                PNG, JPEG -> {
                    val url = urls[ZERO].toString().replace(PDF_EXTENSION, PNG_EXTENSION).toUri()
                    val address = getAddressOfFile(requireContext(), url)
                    list.add(address)

                    sharingIntent.apply {
                        type = getIntentFileType(fileType)
                        putExtra(Intent.EXTRA_STREAM, url.toString())
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                }

                else -> {
                    for (url in urls) {
                        list.add(getAddressOfFile(requireContext(), url))
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

        fun getAddressOfFile(context: Context, uri: Uri?) = if (uri != null) {
            FileProvider.getUriForFile(
                context,
                "${BuildConfig.APPLICATION_ID}.$PROVIDER",
                File(uri.path.toString())
            )
        }else {
            null
        }

        private const val PDF_EXTENSION = ".pdf"
        private const val PNG_EXTENSION = ".png"
        private const val DOCX_EXTENSION = ".docx"
        private const val PROVIDER = "provider"

        private const val SHARE_DATE_USING = "Share data using"
        const val TAG = "ACTION_STAFF_CAMERA"
        private const val FILE_NAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        const val PICTURES_SLASH_CAMERAX_IMAGE = "Pictures/CameraX-Image"
    }
}
