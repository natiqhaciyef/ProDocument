package com.natiqhaciyef.prodocument.ui.view.scan

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ExperimentalGetImage
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.natiqhaciyef.prodocument.databinding.FragmentScanBinding
import com.natiqhaciyef.prodocument.ui.base.BaseFragment
import com.natiqhaciyef.prodocument.ui.base.BaseNavigationDeepLink.HOME_ROUTE
import com.natiqhaciyef.prodocument.ui.view.scan.behaviour.CameraTypes
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalGetImage
@AndroidEntryPoint
class ScanFragment : BaseFragment<FragmentScanBinding>() {
    private val scanViewModel: ScanViewModel by viewModels()

    private val registerForPermissionResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startCameraConfig()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            goBackIcon.setOnClickListener { navigateByRouteTitle(HOME_ROUTE) }
        }
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
            startCameraConfig()
        } else {
            registerForPermissionResult.launch(Manifest.permission.CAMERA)
        }
    }


    private fun startCameraConfig() {
        scanViewModel.startCamera(
            requireContext(),
            viewLifecycleOwner,
            binding.cameraXPreviewHolder,
            CameraTypes.QR_CODE_SCREEN,
        ) { value ->
            value as String

        }

        scanViewModel.startCamera(
            requireContext(),
            viewLifecycleOwner,
            binding.cameraXPreviewHolderBackground,
            CameraTypes.QR_CODE_SCREEN,
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}