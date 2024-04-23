package com.natiqhaciyef.prodocument.ui.view.main.home.options.scan.viewmodel

import android.content.Context
import android.net.Uri
import android.view.View
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.view.PreviewView
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.helpers.getNow
import com.natiqhaciyef.common.model.Status
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.prodocument.ui.util.CameraReader
import com.natiqhaciyef.domain.usecase.qrCode.ReadQrCodeResultUseCase
import com.natiqhaciyef.domain.worker.config.PDF
import com.natiqhaciyef.prodocument.ui.base.BaseViewModel
import com.natiqhaciyef.prodocument.ui.view.main.home.options.scan.behaviour.CameraTypes
import com.natiqhaciyef.prodocument.ui.view.main.home.options.scan.contract.ScanContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@ExperimentalGetImage
@HiltViewModel
class ScanViewModel @Inject constructor(
    private val readQrCodeResultUseCase: ReadQrCodeResultUseCase
) : BaseViewModel<ScanContract.ScanState, ScanContract.ScanEvent, ScanContract.ScanEffect>() {
    private val cameraReaderLiveData = MutableLiveData<CameraReader?>(null)
    var isBackPressed: MutableLiveData<Boolean> = MutableLiveData(false)

    override fun onEventUpdate(event: ScanContract.ScanEvent) {
        when (event) {
            is ScanContract.ScanEvent.ReadQrCodeEvent -> {
                readQrCode(event.qrCode)
            }

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
                    title = event.title ?: "",
                    uri = event.uri ?: "".toUri(),
                    image = event.image ?: ""
                )
            }

            is ScanContract.ScanEvent.StartQrCameraEvent -> {
                startCamera(
                    event.context,
                    event.lifecycle,
                    event.preview,
                    event.type,
                    event.view,
                    event.onSuccess
                )
            }

            is ScanContract.ScanEvent.ClearStateEvent -> {
                setBaseState(getCurrentBaseState().copy(isLoading = false, result = null, material = null))
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


    private fun readQrCode(qrCode: String) {
        viewModelScope.launch {
            readQrCodeResultUseCase.operate(qrCode).collectLatest { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        if (result.data != null)
                            setBaseState(getCurrentBaseState().copy(isLoading = false, result = result.data))
                    }

                    Status.ERROR -> {
                        setBaseState(getCurrentBaseState().copy(isLoading = false))
                        postEffect(
                            ScanContract.ScanEffect.ReadQrCodeFailedEffect(
                            result.message,
                            result.exception
                        ))
                    }

                    Status.LOADING -> {
                        setBaseState(getCurrentBaseState().copy(isLoading = true))
                    }
                }
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
            isLoading = false,
            result = null
        ))
    }


    override fun getInitialState(): ScanContract.ScanState = ScanContract.ScanState()
}