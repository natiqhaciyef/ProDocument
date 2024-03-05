package com.natiqhaciyef.prodocument.ui.view.scan

import android.Manifest
import android.content.Intent
import androidx.fragment.app.viewModels
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore.Images.Media
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ExperimentalGetImage
import androidx.core.content.ContextCompat
import com.natiqhaciyef.prodocument.common.helpers.loadImage
import com.natiqhaciyef.prodocument.databinding.FragmentCaptureImageBinding
import com.natiqhaciyef.prodocument.ui.base.BaseFragment
import com.natiqhaciyef.prodocument.ui.base.BaseNavigationDeepLink
import com.natiqhaciyef.prodocument.ui.view.scan.behaviour.CameraTypes
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalGetImage
@AndroidEntryPoint
class CaptureImageFragment : BaseFragment() {
    private var _binding: FragmentCaptureImageBinding? = null
    private val binding: FragmentCaptureImageBinding
        get() = _binding!!

    private val scanViewModel: ScanViewModel by viewModels()
    private var imageUri: Uri? = null

    private val registerForCameraPermissionResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startCamera()
                captureImageAction()
            }
        }
    private val registerForStoragePermissionResult =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { grantResults ->
            if (grantResults.isNotEmpty()) {
                if (grantResults[Manifest.permission.READ_EXTERNAL_STORAGE] == true
                    && grantResults[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true
                ) {
//                    action
                }
            }
        }
    private val actionForGetImageFromStorage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            result?.let { activityResult ->
                activityResult.data?.let { intent ->
                    imageUri = intent.data
                    imageLoadingAction(imageUri)
                }
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
        binding.goBackIcon.setOnClickListener { goBackIconAction() }
        imagePickFromGalleryAction()
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
            registerForCameraPermissionResult.launch(Manifest.permission.CAMERA)
        }
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

    private fun startCamera(
        view: View? = null,
    ) {
        scanViewModel.startCamera(
            requireContext(),
            viewLifecycleOwner,
            binding.cameraXPreviewHolder,
            CameraTypes.CAPTURE_IMAGE_SCREEN,
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


    // gallery actions
    private fun checkExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
//            action
            val intent = Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI)
            actionForGetImageFromStorage.launch(intent)
        } else {
            registerForStoragePermissionResult.launch(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            )
        }
    }

    private fun imagePickFromGalleryAction() {
        val view = ScanTypeFragment.galleryButtonClickAction()
        view?.setOnClickListener { checkExternalStoragePermission() }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}