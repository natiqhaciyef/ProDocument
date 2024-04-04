package com.natiqhaciyef.prodocument.ui.view.options.scan

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore.Images.Media
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ExperimentalGetImage
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import com.natiqhaciyef.common.helpers.loadImage
import com.natiqhaciyef.prodocument.ui.util.CameraReader
import com.natiqhaciyef.prodocument.databinding.FragmentCaptureImageBinding
import com.natiqhaciyef.prodocument.ui.base.BaseFragment
import com.natiqhaciyef.prodocument.ui.base.BaseNavigationDeepLink.HOME_ROUTE
import com.natiqhaciyef.prodocument.ui.view.options.scan.behaviour.CameraTypes
import com.natiqhaciyef.prodocument.ui.view.options.scan.viewmodel.ScanViewModel
import dagger.hilt.android.AndroidEntryPoint

//val inset = context.convertDpToPixel(16)

@ExperimentalGetImage
@AndroidEntryPoint
class CaptureImageFragment : BaseFragment<FragmentCaptureImageBinding, ScanViewModel>(
    FragmentCaptureImageBinding::inflate,
    ScanViewModel::class
) {
    private var imageUri: Uri? = null

    private var scanner =
        GmsDocumentScanning.getClient(CameraReader.cameraScannerDefaultOptions)

    private val scannerLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val resultForPDF =
                    GmsDocumentScanningResult.fromActivityResultIntent(result.data)

                if (resultForPDF != null) {
                    val material = viewModel?.createMaterial(
                        title = "Scanned file title",
                        uri = resultForPDF.pdf?.uri?.path.toString().toUri(),
                        image = "${resultForPDF.pages?.map { it.imageUri.path.toString() }?.first()}"
                    )

                    val action =
                        ScanTypeFragmentDirections.actionScanTypeFragmentToModifyPdfFragment(
                            material,
                            CAPTURE_IMAGE_TYPE
                        )
                    navigate(action)
                }
            }
        }

    private val registerForCameraPermissionResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startCamera()
                imageScanningAction()
            }
        }
    private val registerForStoragePermissionResult =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { grantResults ->
            if (grantResults.isNotEmpty()) {
                if (grantResults[Manifest.permission.READ_EXTERNAL_STORAGE] == true
                    && grantResults[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true
                ) {

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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.goBackIcon.setOnClickListener { goBackIconAction() }
        imagePickFromGalleryAction()

        requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateByRouteTitle(HOME_ROUTE)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel?.isBackPressed?.value?.let {
            println(it)
            if (!it) {
                checkCameraPermission()
                viewModel?.isBackPressed?.value = true
            } else {
                navigateByRouteTitle(HOME_ROUTE)
                viewModel?.isBackPressed?.value = false
            }
        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            imageScanningAction()
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
                navigateByRouteTitle(HOME_ROUTE)
            }
        }
    }

    private fun startCamera(
        view: View? = null,
    ) {
        viewModel?.startCamera(
            requireContext(),
            viewLifecycleOwner,
            binding.cameraXPreviewHolder,
            CameraTypes.CAPTURE_IMAGE_SCREEN,
            view,
        ) { uri ->
            uri as Uri
            println(uri)
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

    private fun imageScanningAction() {
        scanner.getStartScanIntent(requireActivity())
            .addOnSuccessListener { intentSender ->
                scannerLauncher.launch(IntentSenderRequest.Builder(intentSender).build())
            }
            .addOnFailureListener {
                println(it.localizedMessage)
            }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object{
        const val CAPTURE_IMAGE_TYPE = "capture-image-type"
    }
}