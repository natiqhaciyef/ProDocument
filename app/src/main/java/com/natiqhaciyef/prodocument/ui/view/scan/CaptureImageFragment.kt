package com.natiqhaciyef.prodocument.ui.view.scan

import android.Manifest
<<<<<<< HEAD
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
=======
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.text.Text
>>>>>>> origin/upgrade
import com.natiqhaciyef.prodocument.databinding.FragmentCaptureImageBinding
import com.natiqhaciyef.prodocument.ui.base.BaseFragment
import com.natiqhaciyef.prodocument.ui.view.scan.behaviour.CameraTypes
import dagger.hilt.android.AndroidEntryPoint
<<<<<<< HEAD
=======
import java.util.Locale
import java.util.concurrent.Executors
>>>>>>> origin/upgrade

@ExperimentalGetImage
@AndroidEntryPoint
class CaptureImageFragment : BaseFragment() {
    private var _binding: FragmentCaptureImageBinding? = null
    private val binding: FragmentCaptureImageBinding
        get() = _binding!!

    private val scanViewModel: ScanViewModel by viewModels()
    private val registerForPermissionResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startCamera()
                captureImageAction()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCaptureImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        checkCameraPermission()
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            captureImageAction()
        } else {
            registerForPermissionResult.launch(Manifest.permission.CAMERA)
        }
    }

    private fun captureImageAction() {
        val view = ScanTypeFragment.buttonClickAction()
        startCamera(view)
    }

    private fun startCamera(view: View? = null) {
        scanViewModel.startCamera(
            requireContext(),
            viewLifecycleOwner,
            binding.cameraXPreviewHolder,
            CameraTypes.CAPTURE_IMAGE_SCREEN,
            view
        ){ value ->
            value as Uri


        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}