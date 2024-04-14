package com.natiqhaciyef.prodocument.ui.view.options.scan

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ExperimentalGetImage
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.natiqhaciyef.common.R
import com.natiqhaciyef.prodocument.databinding.FragmentScanBinding
import com.natiqhaciyef.prodocument.ui.base.BaseFragment
import com.natiqhaciyef.prodocument.ui.base.BaseNavigationDeepLink.HOME_ROUTE
import com.natiqhaciyef.prodocument.ui.view.options.scan.behaviour.CameraTypes
import com.natiqhaciyef.prodocument.ui.view.options.scan.event.ScanEvent
import com.natiqhaciyef.prodocument.ui.view.options.scan.viewmodel.ScanViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

@ExperimentalGetImage
@AndroidEntryPoint
class ScanFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentScanBinding = FragmentScanBinding::inflate,
    override val viewModelClass: KClass<ScanViewModel> = ScanViewModel::class
) : BaseFragment<FragmentScanBinding, ScanViewModel, ScanEvent>() {
    private var selectedImage: Uri? = null
    private val registerForCameraPermissionResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startCameraConfig()
            }
        }
    private val registerForGalleryPermissionResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted)
                startGalleryConfig()
        }
    private val launchGallerySelection =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let {
                    selectedImage = it
                    navigateToModifyPdf()
                }
            }
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.goBackIcon.setOnClickListener { navigateByRouteTitle(HOME_ROUTE) }

        ScanTypeFragment.galleryButtonClickAction.invoke()?.setOnClickListener {
            checkGalleryPermission()
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
            registerForCameraPermissionResult.launch(Manifest.permission.CAMERA)
        }
    }


    private fun startCameraConfig() {
        viewModel?.startCamera(
            requireContext(),
            viewLifecycleOwner,
            binding.cameraXPreviewHolder,
            CameraTypes.QR_CODE_SCREEN,
        ) { value ->
            value as String
            // logic of qr scan
//            viewModel?.apply {
//                readQrCode(qrCode = value)
//
//                qrCodeLiveData.observe(viewLifecycleOwner) {
//                    // action after scanned success
//                }
//            }
        }

        viewModel?.startCamera(
            requireContext(),
            viewLifecycleOwner,
            binding.cameraXPreviewHolderBackground,
            CameraTypes.QR_CODE_SCREEN,
        )
    }

    private fun checkGalleryPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // go to gallery
            startGalleryConfig()
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                Snackbar.make(
                    requireView(),
                    NEED_GALLERY_PERMISSION,
                    Snackbar.LENGTH_LONG
                )
                    .setAction(
                        requireContext()
                            .getString(R.string.give_permission)
                    ) { registerForGalleryPermissionResult.launch(Manifest.permission.READ_EXTERNAL_STORAGE) }
                    .show()

            } else {
                registerForGalleryPermissionResult.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private fun startGalleryConfig() {
        val intent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        launchGallerySelection.launch(intent)
    }

    private fun navigateToModifyPdf() {
        selectedImage?.let {
            val material = viewModel?.createMaterial(
                title = "Selected image",
                uri = it,
                image = it.toString()
            )?.copy()

            val action =
                ScanTypeFragmentDirections.actionScanTypeFragmentToModifyPdfFragment(
                    material,
                    SCAN_QR_TYPE
                )
            navigate(action)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val NEED_GALLERY_PERMISSION = "Need gallery permission"
        private const val NEED_FILE_PERMISSION = "Need file permission"
        const val SCAN_QR_TYPE = "scan-qr-type"
    }
}