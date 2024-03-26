package com.natiqhaciyef.prodocument.ui.util

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
import com.natiqhaciyef.domain.worker.config.JPEG
import com.natiqhaciyef.domain.worker.config.PDF
import com.natiqhaciyef.domain.worker.config.PNG
import com.natiqhaciyef.domain.worker.config.getIntentFileType
import java.io.File
import java.util.Locale
import java.util.concurrent.Executors


class CameraReader(
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
                Log.e(ContentValues.TAG, "Use case binding failed", exc)
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
                    val barcode = barcodeList.getOrNull(0)
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
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
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
                            Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                        }

                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                            val msg = "Photo capture succeeded: ${output.savedUri}"
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
                Log.e(TAG, "Use case binding failed", exc)
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
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
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
                            Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                        }

                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                            val msg = "Photo capture succeeded: ${output.savedUri}"
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
                Log.e(TAG, "Use case binding failed", exc)
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
            .setPageLimit(30)
            .setResultFormats(RESULT_FORMAT_PDF, RESULT_FORMAT_JPEG)
            .setScannerMode(SCANNER_MODE_FULL)
            .build()

        const val TAG = "ACTION_STAFF_CAMERA"
        private const val FILE_NAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"


        fun Fragment.createAndShareFile(
            fileType: String,
            urls: List<String>,
            isShare: Boolean = true
        ) = when (fileType) {
            PDF -> {
                shareFile(urls, PDF, isShare)
            }

            JPEG -> {
                shareFile(urls, JPEG, isShare)
            }

            PNG -> {
                shareFile(urls, PNG, isShare)
            }

            else -> {
                listOf()
            }
        }

        private fun Fragment.shareFile(
            urls: List<String>,
            fileType: String,
            isShare: Boolean = true
        ): List<Uri?> {
            val list = mutableListOf<Uri?>()
            val sharingIntent = Intent(Intent.ACTION_SEND)

            if (urls.size == 1) {
                val externalUri = getAddressOfFile(requireContext(), urls[0])
                if (isShare)
                    sharingIntent.apply {
                        type = getIntentFileType(fileType)
                        putExtra(Intent.EXTRA_STREAM, externalUri)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                list.add(externalUri)
            } else {

                for (url in urls) {
                    list.add(getAddressOfFile(requireContext(), url))
                }

                if (isShare)
                    sharingIntent.apply {
                        type = getIntentFileType(fileType)
                        putExtra(Intent.EXTRA_STREAM, list.toTypedArray())
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
            }

            startActivity(Intent.createChooser(sharingIntent, "Share data using"))
            return list
        }

        fun getAddressOfFile(context: Context, uri: String?) = if (uri != null)
            FileProvider.getUriForFile(
                context,
                "${BuildConfig.APPLICATION_ID}.provider",
                File(uri)
            )
        else
            null
    }
}