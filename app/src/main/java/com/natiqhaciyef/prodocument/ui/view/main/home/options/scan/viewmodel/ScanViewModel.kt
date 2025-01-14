package com.natiqhaciyef.prodocument.ui.view.main.home.options.scan.viewmodel

import android.content.Context
import android.net.Uri
import android.view.View
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.view.PreviewView
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.natiqhaciyef.common.constants.EMPTY_STRING
import com.natiqhaciyef.common.helpers.getNow
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.ui.BaseViewModel
import com.natiqhaciyef.core.model.FileTypes.PDF
import com.natiqhaciyef.prodocument.ui.util.CameraUtil
import com.natiqhaciyef.prodocument.ui.view.main.home.options.scan.behaviour.CameraTypes
import com.natiqhaciyef.prodocument.ui.view.main.home.options.scan.contract.ScanContract
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject

@ExperimentalGetImage
@HiltViewModel
class ScanViewModel @Inject constructor() : BaseViewModel<ScanContract.ScanState, ScanContract.ScanEvent, ScanContract.ScanEffect>() {
    private val cameraReaderLiveData = MutableLiveData<CameraUtil?>(null)
    var isBackPressed: MutableLiveData<Boolean> = MutableLiveData(false)

    override fun onEventUpdate(event: ScanContract.ScanEvent) {
        when (event) {
            is ScanContract.ScanEvent.StartCameraEvent -> {
                startCamera(
                    event.context,
                    event.lifecycle,
                    event.preview,
                    event.type,
                    event.view,
                    event.onSuccess
                )
            }

            is ScanContract.ScanEvent.CreateMaterialEvent -> {
                createMaterial(
                    title = event.title ?: EMPTY_STRING,
                    uri = event.uri ?: EMPTY_STRING.toUri(),
                    image = event.image ?: EMPTY_STRING
                )
            }


            is ScanContract.ScanEvent.ClearStateEvent -> {
                setBaseState(getCurrentBaseState().copy(isLoading = false, material = null))
            }
        }
    }

    private fun startCamera(
        context: Context,
        lifecycle: LifecycleOwner,
        preview: PreviewView,
        type: CameraTypes,
        view: View? = null,
        onSuccess: (Any) -> Unit = { }
    ) {
        if (cameraReaderLiveData.value == null) {
            cameraReaderLiveData.value = CameraUtil(context, lifecycle)
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

    private fun createMaterial(
        title: String,
        uri: Uri,
        image: String
    ){
        setBaseState(getCurrentBaseState().copy(
            material = MappedMaterialModel(
                id = "${UUID.randomUUID()}",
                image = image,
                title = title,
                description = null,
                createdDate = getNow(),
                type = PDF,
                url = uri,
                downloadedUri = null,
                isDownloading = false
            ),
            isLoading = false
        ))
    }


    override fun getInitialState(): ScanContract.ScanState = ScanContract.ScanState()
}