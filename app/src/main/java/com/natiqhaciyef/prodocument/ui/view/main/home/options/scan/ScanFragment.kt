package com.natiqhaciyef.prodocument.ui.view.main.home.options.scan

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
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.prodocument.databinding.FragmentScanBinding
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.ui.manager.PermissionManager
import com.natiqhaciyef.prodocument.ui.util.NavigationManager.HOME_ROUTE
import com.natiqhaciyef.prodocument.ui.util.NavigationManager.navigateByRouteTitle
import com.natiqhaciyef.prodocument.ui.util.BundleConstants.BUNDLE_MATERIAL
import com.natiqhaciyef.prodocument.ui.util.BundleConstants.BUNDLE_TYPE
import com.natiqhaciyef.prodocument.ui.view.main.MainActivity
import com.natiqhaciyef.prodocument.ui.view.main.home.options.scan.behaviour.CameraTypes
import com.natiqhaciyef.prodocument.ui.view.main.home.options.scan.contract.ScanContract
import com.natiqhaciyef.prodocument.ui.view.main.home.options.scan.viewmodel.ScanViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

@ExperimentalGetImage
@AndroidEntryPoint
class ScanFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentScanBinding = FragmentScanBinding::inflate,
    override val viewModelClass: KClass<ScanViewModel> = ScanViewModel::class
) : BaseFragment<FragmentScanBinding, ScanViewModel, ScanContract.ScanState, ScanContract.ScanEvent, ScanContract.ScanEffect>() {
    private var bundle = bundleOf()
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
                    createMaterialEvent()
                }
            }
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.postEvent(ScanContract.ScanEvent.ClearStateEvent)

        binding.goBackIcon.setOnClickListener { goBackAction() }
        ScanTypeFragment.galleryButtonClickAction.invoke()?.setOnClickListener {
            checkGalleryPermission()
        }
    }

    override fun onStateChange(state: ScanContract.ScanState) {
        when {
            state.isLoading -> {
                changeVisibilityOfProgressBar(true)
            }

            else -> {
                changeVisibilityOfProgressBar()

                if (state.result != null) {
                    // handle qr reading result
                }

                if (state.material != null) {
                    navigateToModifyPdf(state.material!!)
                }
            }
        }
    }

    override fun onEffectUpdate(effect: ScanContract.ScanEffect) {

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
        viewModel.postEvent(
            ScanContract.ScanEvent.StartQrCameraEvent(
                requireContext(),
                viewLifecycleOwner,
                binding.cameraXPreviewHolder,
                CameraTypes.QR_CODE_SCREEN,
            ) { value ->
                value as String
                viewModel.postEvent(ScanContract.ScanEvent.ReadQrCodeEvent(qrCode = value))
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

    private fun checkGalleryPermission() {
        PermissionManager.Builder(this@ScanFragment, false)
            .addPermissionLauncher(registerForGalleryPermissionResult)
            .request(PermissionManager.Permission.createCustomPermission(Manifest.permission.READ_EXTERNAL_STORAGE))
            .checkPermission { startGalleryConfig() }
            .build()
    }

    private fun startGalleryConfig() {
        val intent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        launchGallerySelection.launch(intent)
    }

    private fun navigateToModifyPdf(material: MappedMaterialModel) {
        bundle.putString(BUNDLE_TYPE, SCAN_QR_TYPE)
        bundle.putParcelable(BUNDLE_MATERIAL, material)

        val action =
            ScanTypeFragmentDirections.actionScanTypeFragmentToPreviewMaterialNavGraph(bundle)
        navigate(action)
    }

    private fun createMaterialEvent() {
        selectedImage?.let {
            viewModel.postEvent(
                ScanContract.ScanEvent.CreateMaterialEvent(
                    title = "Selected image",
                    uri = it,
                    image = it.toString()
                )
            )
        }
    }

    private fun goBackAction() {
        (activity as MainActivity).apply {
            binding.bottomNavBar.visibility = View.VISIBLE
            binding.appbarLayout.visibility = View.VISIBLE
        }
        navigateByRouteTitle(this@ScanFragment, HOME_ROUTE)
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