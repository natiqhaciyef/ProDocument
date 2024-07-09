package com.natiqhaciyef.prodocument.ui.view.main.home.options.scan

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ExperimentalGetImage
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.natiqhaciyef.common.helpers.toDetails
import com.natiqhaciyef.common.model.mapped.MappedSubscriptionModel
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.databinding.FragmentScanBinding
import com.natiqhaciyef.prodocument.ui.util.BUNDLE_MATERIAL
import com.natiqhaciyef.prodocument.ui.util.BUNDLE_SUBSCRIPTION_PLAN
import com.natiqhaciyef.prodocument.ui.util.BUNDLE_TYPE
import com.natiqhaciyef.prodocument.ui.util.DefaultImplModels
import com.natiqhaciyef.prodocument.ui.view.main.home.options.scan.behaviour.CameraTypes
import com.natiqhaciyef.prodocument.ui.view.main.home.options.scan.contract.ScanContract
import com.natiqhaciyef.prodocument.ui.view.main.home.options.scan.viewmodel.ScanViewModel
import com.natiqhaciyef.prodocument.ui.view.main.payment.contract.PaymentContract
import com.natiqhaciyef.uikit.manager.PermissionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

@ExperimentalGetImage
@AndroidEntryPoint
class ScanFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentScanBinding = FragmentScanBinding::inflate,
    override val viewModelClass: KClass<ScanViewModel> = ScanViewModel::class
) : BaseFragment<FragmentScanBinding, ScanViewModel, ScanContract.ScanState, ScanContract.ScanEvent, ScanContract.ScanEffect>() {
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
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let {
                    selectedImage = it
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ScanTypeFragment.galleryButtonClickAction()
            ?.setOnClickListener { checkGalleryPermission() }
        binding.goBackIcon.setOnClickListener { navigateBack() }
    }

    override fun onResume() {
        super.onResume()
        checkCameraPermission()
    }


    override fun onStateChange(state: ScanContract.ScanState) {
        when {
            state.isLoading -> {

            }

            else -> {

            }
        }
    }

    override fun onEffectUpdate(effect: ScanContract.ScanEffect) {

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
        viewModel.postEvent(
            ScanContract.ScanEvent.StartCameraEvent(
                requireContext(),
                viewLifecycleOwner,
                binding.cameraXPreviewHolder,
                CameraTypes.QR_CODE_SCREEN,
                ScanTypeFragment.captureButtonClickAction()
            ) { value ->
                value as String
                println(value)
                afterPhotoCaptured(value)
            }
        )

        viewModel.postEvent(
            ScanContract.ScanEvent.StartCameraEvent(
                requireContext(),
                viewLifecycleOwner,
                binding.cameraXPreviewHolderBackground,
                CameraTypes.QR_CODE_SCREEN
            )
        )
    }

    private fun afterPhotoCaptured(result: String) {
        // INSERT: Send backend
    }

    private fun checkGalleryPermission() {
        PermissionManager.Builder(this@ScanFragment, false)
            .addPermissionLauncher(registerForGalleryPermissionResult)
            .request(PermissionManager.Permission.createCustomPermission(Manifest.permission.READ_EXTERNAL_STORAGE))
            .checkPermission { startGalleryConfig() }
            .build()
    }

    private fun startGalleryConfig() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        launchGallerySelection.launch(intent)
    }


}