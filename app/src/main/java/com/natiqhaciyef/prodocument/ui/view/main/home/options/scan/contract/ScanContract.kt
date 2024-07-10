package com.natiqhaciyef.prodocument.ui.view.main.home.options.scan.contract

import android.content.Context
import android.net.Uri
import android.view.View
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.ui.UiEffect
import com.natiqhaciyef.core.base.ui.UiEvent
import com.natiqhaciyef.core.base.ui.UiState
import com.natiqhaciyef.prodocument.ui.view.main.home.options.scan.behaviour.CameraTypes

object ScanContract {

    sealed interface ScanEvent : UiEvent {
        data class StartCameraEvent(
            var context: Context,
            var lifecycle: LifecycleOwner,
            var preview: PreviewView,
            var type: CameraTypes,
            var view: View? = null,
            var onSuccess: (Any) -> Unit = { }
        ) : ScanEvent

        data class CreateMaterialEvent(
            var title: String? = null,
            var uri: Uri? = null,
            var image: String? = null
        ) : ScanEvent

        data object ClearStateEvent : ScanEvent
    }

    sealed interface ScanEffect : UiEffect {
        data class ReadQrCodeFailedEffect(
            var message: String? = null,
            var exception: Exception? = null
        ): ScanEffect
    }

    data class ScanState(
        override var isLoading: Boolean = false,
        var material: MappedMaterialModel? = null
        ) : UiState
}
