package com.natiqhaciyef.prodocument.ui.view.scan

import android.content.Context
import android.net.Uri
import android.view.View
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.natiqhaciyef.common.helpers.getNow
import com.natiqhaciyef.prodocument.ui.util.CameraReader
import com.natiqhaciyef.common.worker.config.PDF
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.prodocument.ui.base.BaseViewModel
import com.natiqhaciyef.prodocument.ui.view.scan.behaviour.CameraTypes
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject

@ExperimentalGetImage
@HiltViewModel
class ScanViewModel @Inject constructor(

) : BaseViewModel() {
    private val cameraReaderLiveData = MutableLiveData<CameraReader?>(null)

    fun startCamera(
        context: Context,
        lifecycle: LifecycleOwner,
        preview: PreviewView,
        type: CameraTypes,
        view: View? = null,
        onSuccess: (Any) -> Unit = { }
    ) {
        if (cameraReaderLiveData.value == null) {
            cameraReaderLiveData.value = CameraReader(context, lifecycle)
        }

        when (type.name) {
            CameraTypes.QR_CODE_SCREEN.name -> {
                cameraReaderLiveData.value?.openBarcodeScanner(preview, onSuccess)
            }

            CameraTypes.LIVE_SCANNER.name -> {
                cameraReaderLiveData.value?.openLiveTextRecognizer(preview, view, onSuccess)
            }

            CameraTypes.CAPTURE_IMAGE_SCREEN.name -> {
                cameraReaderLiveData.value?.openCapturedImageTextRecognizer(
                    preview,
                    view,
                    onSuccess
                )
            }
        }
    }

    fun createMaterial(
        title: String,
        uri: Uri,
        image: String
    ) = MappedMaterialModel(
        id = "${UUID.randomUUID()}",
        image = image,
        title = title,
        description = null,
        createdDate = getNow(),
        type = PDF,
        url = uri.toString(),
        downloadedUri = null,
        isDownloading = false
    )

}