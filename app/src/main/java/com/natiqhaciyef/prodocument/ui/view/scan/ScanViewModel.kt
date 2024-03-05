package com.natiqhaciyef.prodocument.ui.view.scan

import android.content.Context
import android.view.View
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.natiqhaciyef.prodocument.common.worker.camera.CameraReader
import com.natiqhaciyef.prodocument.ui.base.BaseViewModel
import com.natiqhaciyef.prodocument.ui.view.scan.behaviour.CameraTypes
import dagger.hilt.android.lifecycle.HiltViewModel
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

            CameraTypes.CAPTURE_IMAGE_SCREEN.name -> {
                val options = cameraReaderLiveData.value?.openCameraScannerOptions()
                options?.let(onSuccess)
            }

            CameraTypes.LIVE_SCANNER.name -> {
                cameraReaderLiveData.value?.openLiveTextRecognizer(preview, view, onSuccess)
            }
        }
    }
}