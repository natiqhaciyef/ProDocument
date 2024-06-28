package com.natiqhaciyef.prodocument.ui.view.main.home.options.scan

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ExperimentalGetImage
import androidx.core.content.ContextCompat
import com.natiqhaciyef.common.helpers.loadImage
import com.natiqhaciyef.prodocument.databinding.FragmentLiveRecognitionBinding
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.ui.manager.NavigationManager
import com.natiqhaciyef.prodocument.ui.manager.NavigationManager.navigateByRouteTitle
import com.natiqhaciyef.prodocument.ui.view.main.home.options.scan.behaviour.CameraTypes
import com.natiqhaciyef.prodocument.ui.view.main.home.options.scan.contract.ScanContract
import com.natiqhaciyef.prodocument.ui.view.main.home.options.scan.viewmodel.ScanViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

@ExperimentalGetImage
@AndroidEntryPoint
class LiveRecognitionFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLiveRecognitionBinding = FragmentLiveRecognitionBinding::inflate,
    override val viewModelClass: KClass<ScanViewModel> = ScanViewModel::class
) : BaseFragment<FragmentLiveRecognitionBinding, ScanViewModel, ScanContract.ScanState, ScanContract.ScanEvent, ScanContract.ScanEffect>() {
    private val registerForCameraPermissionResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startCameraEvent()
                captureImageAction()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.postEvent(ScanContract.ScanEvent.ClearStateEvent)
        binding.goBackIcon.setOnClickListener { goBackIconAction() }
    }

    override fun onResume() {
        super.onResume()
        checkCameraPermission()
    }

    private fun goBackIconAction() {
        binding.apply {
            if (capturedImage.visibility == View.VISIBLE) {
                capturedImage.visibility = View.GONE
                scanTitle.visibility = View.VISIBLE
                scanDescription.visibility = View.VISIBLE
            } else {
                navigateByRouteTitle(this@LiveRecognitionFragment, NavigationManager.HOME_ROUTE)
            }
        }
    }


    // capturing image
    private fun captureImageAction() {
        val view = ScanTypeFragment.captureButtonClickAction()
        startCameraEvent(view)
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            captureImageAction()
        } else {
            registerForCameraPermissionResult.launch(Manifest.permission.CAMERA)
        }
    }

    private fun startCameraEvent(
        view: View? = null,
    ) {
        viewModel.postEvent(
            ScanContract.ScanEvent.StartCameraEvent(
                requireContext(),
                viewLifecycleOwner,
                binding.cameraXPreviewHolder,
                CameraTypes.LIVE_SCANNER,
                view
            ){ uri ->
                uri as Uri
                imageLoadingAction(uri)
//                after live recognition
            }
        )
    }

    private fun imageLoadingAction(uri: Uri?) {
        uri?.let {
            binding.capturedImage.visibility = View.VISIBLE
            binding.scanTitle.visibility = View.GONE
            binding.scanDescription.visibility = View.GONE

            binding.capturedImage.loadImage(uri.toString(), requireContext())
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}