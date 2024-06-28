package com.natiqhaciyef.prodocument.ui.view.main.payment

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
import androidx.navigation.fragment.navArgs
import com.natiqhaciyef.common.helpers.toDetails
import com.natiqhaciyef.common.model.mapped.MappedSubscriptionModel
import com.natiqhaciyef.common.model.payment.MappedPaymentModel
import com.natiqhaciyef.prodocument.databinding.FragmentScanBinding
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.ui.manager.PermissionManager
import com.natiqhaciyef.prodocument.ui.util.BUNDLE_CHEQUE_PAYMENT
import com.natiqhaciyef.prodocument.ui.util.BUNDLE_PAYMENT
import com.natiqhaciyef.prodocument.ui.util.BUNDLE_SUBSCRIPTION_PLAN
import com.natiqhaciyef.prodocument.ui.view.main.MainActivity
import com.natiqhaciyef.prodocument.ui.view.main.payment.contract.PaymentContract
import com.natiqhaciyef.prodocument.ui.view.main.payment.viewmodel.PaymentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

@ExperimentalGetImage
@AndroidEntryPoint
class ScanFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentScanBinding = FragmentScanBinding::inflate,
    override val viewModelClass: KClass<PaymentViewModel> = PaymentViewModel::class
) : BaseFragment<FragmentScanBinding, PaymentViewModel, PaymentContract.PaymentState, PaymentContract.PaymentEvent, PaymentContract.PaymentEffect>() {
    private var resourceBundle = bundleOf()
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
                }
            }
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: ScanFragmentArgs by navArgs()
        resourceBundle = args.datasetBundle
        activityConfig()
        with(binding) {
            goBackIcon.setOnClickListener { navigateBack() }
            pickFromGalleryButton.setOnClickListener { checkGalleryPermission() }
        }
    }

    override fun onStateChange(state: PaymentContract.PaymentState) {
        when {
            state.isLoading -> {
                changeVisibilityOfProgressBar(true)
            }

            else -> {
                changeVisibilityOfProgressBar()

                state.qrCodePaymentModel?.let {
                    val cheque = it.cheque
                    val paymentModel = MappedPaymentModel(
                        merchantId = it.merchantId,
                        paymentMethod = it.paymentMethod,
                        paymentType = it.paymentType,
                        paymentDetails = it.cheque.paymentDetails
                    )

                    resourceBundle.putParcelable(BUNDLE_CHEQUE_PAYMENT, cheque)
                    resourceBundle.putParcelable(BUNDLE_PAYMENT, paymentModel)

                    val action = ScanFragmentDirections.actionScanFragmentToPaymentDetailsFragment(resourceBundle)
                    navigate(action)
                }
            }
        }
    }

    override fun onEffectUpdate(effect: PaymentContract.PaymentEffect) {

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

    private fun activityConfig() {
        (activity as MainActivity).binding.materialToolbar.visibility = View.GONE
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
            PaymentContract.PaymentEvent.StartCamera(
                requireContext(),
                viewLifecycleOwner,
                binding.cameraXPreviewHolder,
            ) { value ->
                value as String

                val subscription =
                    resourceBundle.getParcelable<MappedSubscriptionModel>(BUNDLE_SUBSCRIPTION_PLAN)
                val subscriptionDetails = subscription?.toDetails()

                if (subscriptionDetails != null)
                    viewModel.postEvent(
                        PaymentContract.PaymentEvent.ScanQRCode(
                            qrCode = value,
                            subscriptionPlanPaymentDetails = subscriptionDetails
                        )
                    )
            }
        )

        viewModel.postEvent(
            PaymentContract.PaymentEvent.StartCamera(
                requireContext(),
                viewLifecycleOwner,
                binding.cameraXPreviewHolderBackground,
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


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val SCAN_QR_TYPE = "scan-qr-type"
    }
}