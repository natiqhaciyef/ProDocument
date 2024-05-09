package com.natiqhaciyef.prodocument.ui.view.main.home.options.scan

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore.Images.Media
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import com.natiqhaciyef.common.helpers.loadImage
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.prodocument.ui.util.CameraReader
import com.natiqhaciyef.prodocument.databinding.FragmentCaptureImageBinding
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.ui.util.BaseNavigationDeepLink.HOME_ROUTE
import com.natiqhaciyef.prodocument.ui.util.BaseNavigationDeepLink.navigateByRouteTitle
import com.natiqhaciyef.prodocument.ui.util.BundleConstants.BUNDLE_MATERIAL
import com.natiqhaciyef.prodocument.ui.util.BundleConstants.BUNDLE_TYPE
import com.natiqhaciyef.prodocument.ui.view.main.home.options.scan.behaviour.CameraTypes
import com.natiqhaciyef.prodocument.ui.view.main.home.options.scan.contract.ScanContract
import com.natiqhaciyef.prodocument.ui.view.main.home.options.scan.viewmodel.ScanViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

//val inset = context.convertDpToPixel(16)

@AndroidEntryPoint
class CaptureImageFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCaptureImageBinding = FragmentCaptureImageBinding::inflate,
    override val viewModelClass: KClass<ScanViewModel> = ScanViewModel::class
) : BaseFragment<FragmentCaptureImageBinding, ScanViewModel, ScanContract.ScanState, ScanContract.ScanEvent, ScanContract.ScanEffect>() {
    private var bundle = bundleOf()
    private var imageUri: Uri? = null
    private var scanner =
        GmsDocumentScanning.getClient(CameraReader.cameraScannerDefaultOptions)

    private val scannerLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val resultForPDF =
                    GmsDocumentScanningResult.fromActivityResultIntent(result.data)

                if (resultForPDF != null) {
                    viewModel.postEvent(
                        ScanContract.ScanEvent.CreateMaterialEvent(
                        title = "Scanned file title",
                        uri = resultForPDF.pdf?.uri?.path.toString().toUri(),
                        image = "${
                            resultForPDF.pages?.map { it.imageUri.path.toString() }?.first()
                        }"
                    ))
                }
            }
        }

    private val registerForCameraPermissionResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startCameraEvent()
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
        viewModel.postEvent(ScanContract.ScanEvent.ClearStateEvent)

        binding.goBackIcon.setOnClickListener { goBackIconAction() }
        imagePickFromGalleryAction()

        requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateByRouteTitle(this@CaptureImageFragment,HOME_ROUTE)
            }
        })
    }

    override fun onStateChange(state: ScanContract.ScanState) {
        when{
            state.isLoading -> {
                changeVisibilityOfProgressBar()
            }

            else ->{
                changeVisibilityOfProgressBar(false)
                if (state.result != null){

                }

                if (state.material != null){
                    imageResultAction(state.material!!)
                }
            }
        }
    }

    override fun onEffectUpdate(effect: ScanContract.ScanEffect) {
//        when(effect){
//
//        }
    }

    private fun changeVisibilityOfProgressBar(isVisible: Boolean = false) {
        if (isVisible) {
            binding.apply {
                uiLayout.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
                progressBar.isIndeterminate = true
            }
        } else {
            binding.apply {
                uiLayout.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                progressBar.isIndeterminate = false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.isBackPressed.value?.let {
            if (!it) {
                checkCameraPermission()
                viewModel.isBackPressed.value = true
            } else {
                navigateByRouteTitle(this@CaptureImageFragment,HOME_ROUTE)
                viewModel.isBackPressed.value = false
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
                navigateByRouteTitle(this@CaptureImageFragment,HOME_ROUTE)
            }
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
            CameraTypes.CAPTURE_IMAGE_SCREEN,
            view
        ) { uri ->
            uri as Uri
            imageLoadingAction(uri)
        })
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

            }
    }

    private fun imageResultAction(material: MappedMaterialModel) {
        bundle.putParcelable(BUNDLE_MATERIAL, material)
        bundle.putString(BUNDLE_TYPE, CAPTURE_IMAGE_TYPE)
        val action =
            ScanTypeFragmentDirections.actionScanTypeFragmentToPreviewMaterialNavGraph(bundle)
        navigate(action)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val CAPTURE_IMAGE_TYPE = "capture-image-type"
    }
}