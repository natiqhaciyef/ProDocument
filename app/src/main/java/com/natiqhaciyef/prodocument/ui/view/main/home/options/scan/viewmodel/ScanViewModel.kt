package com.natiqhaciyef.prodocument.ui.view.main.home.options.scan.viewmodel

import android.content.Context
import android.net.Uri
import android.view.View
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.helpers.getNow
import com.natiqhaciyef.common.model.Status
import com.natiqhaciyef.prodocument.ui.util.CameraReader
import com.natiqhaciyef.domain.worker.config.PDF
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.domain.usecase.qrCode.ReadQrCodeResultUseCase
import com.natiqhaciyef.prodocument.ui.base.BaseUIState
import com.natiqhaciyef.prodocument.ui.base.BaseViewModel
import com.natiqhaciyef.prodocument.ui.view.main.home.options.scan.behaviour.CameraTypes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@ExperimentalGetImage
@HiltViewModel
class ScanViewModel @Inject constructor(
    private val readQrCodeResultUseCase: ReadQrCodeResultUseCase
) : BaseViewModel() {
    private val cameraReaderLiveData = MutableLiveData<CameraReader?>(null)
    private val _qrCodeLiveData = MutableLiveData(BaseUIState<String>())
    val qrCodeLiveData: LiveData<BaseUIState<String>>
        get() = _qrCodeLiveData

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
        url = uri,
        downloadedUri = null,
        isDownloading = false
    )

    fun readQrCode(qrCode: String){
        viewModelScope.launch {
            readQrCodeResultUseCase.operate(qrCode).collectLatest { result ->
                when(result.status){
                    Status.SUCCESS -> {
                        _qrCodeLiveData.value?.apply {
                            obj = result.data?.url ?: ""
                            list = listOf()
                            isLoading = false
                            isSuccess = true
                            message = null
                            failReason = null
                        }
                    }
                    Status.ERROR -> {
                        _qrCodeLiveData.value?.apply {
                            obj = result.data?.url ?: ""
                            list = listOf()
                            isLoading = false
                            isSuccess = false
                            message = result.message
                            failReason = result.exception
                        }
                    }
                    Status.LOADING -> {
                        _qrCodeLiveData.value?.apply {
                            obj = null
                            list = listOf()
                            isLoading = true
                            isSuccess = false
                            message = null
                            failReason = null
                        }
                    }
                }
            }
        }
    }
}