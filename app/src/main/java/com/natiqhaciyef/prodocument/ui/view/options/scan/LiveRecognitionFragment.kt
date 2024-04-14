package com.natiqhaciyef.prodocument.ui.view.options.scan

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
import androidx.fragment.app.viewModels
import com.natiqhaciyef.common.helpers.loadImage
import com.natiqhaciyef.prodocument.databinding.FragmentLiveRecognitionBinding
import com.natiqhaciyef.prodocument.ui.base.BaseFragment
import com.natiqhaciyef.prodocument.ui.base.BaseNavigationDeepLink
import com.natiqhaciyef.prodocument.ui.view.options.scan.behaviour.CameraTypes
import com.natiqhaciyef.prodocument.ui.view.options.scan.event.ScanEvent
import com.natiqhaciyef.prodocument.ui.view.options.scan.viewmodel.ScanViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

@ExperimentalGetImage
@AndroidEntryPoint
class LiveRecognitionFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLiveRecognitionBinding = FragmentLiveRecognitionBinding::inflate,
    override val viewModelClass: KClass<ScanViewModel> = ScanViewModel::class
) : BaseFragment<FragmentLiveRecognitionBinding, ScanViewModel, ScanEvent>() {
    private val registerForCameraPermissionResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startCamera()
                captureImageAction()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                navigateByRouteTitle(BaseNavigationDeepLink.HOME_ROUTE)
            }
        }
    }


    // capturing image
    private fun captureImageAction() {
        val view = ScanTypeFragment.captureButtonClickAction()
        startCamera(view)
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

    private fun startCamera(
        view: View? = null,
    ) {
        viewModel?.startCamera(
            requireContext(),
            viewLifecycleOwner,
            binding.cameraXPreviewHolder,
            CameraTypes.LIVE_SCANNER,
            view,
        ) { uri ->
            uri as Uri
            imageLoadingAction(uri)
        }
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