package com.natiqhaciyef.prodocument.ui.view.main.home.options.scan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.ExperimentalGetImage
import com.google.android.material.tabs.TabLayoutMediator
import com.natiqhaciyef.prodocument.databinding.FragmentScanTypeBinding
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.ui.view.main.home.options.scan.adapter.ScanViewPagerAdapter
import com.natiqhaciyef.prodocument.ui.view.main.home.options.scan.behaviour.CameraTypes
import com.natiqhaciyef.prodocument.ui.view.main.home.options.scan.contract.ScanContract
import com.natiqhaciyef.prodocument.ui.view.main.home.options.scan.viewmodel.ScanViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass


@ExperimentalGetImage
@AndroidEntryPoint
class ScanTypeFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentScanTypeBinding = FragmentScanTypeBinding::inflate,
    override val viewModelClass: KClass<ScanViewModel> = ScanViewModel::class
) : BaseFragment<FragmentScanTypeBinding, ScanViewModel, ScanContract.ScanState, ScanContract.ScanEvent, ScanContract.ScanEffect>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPagerInitConfig()
        captureButtonClickAction = { binding.captureImageIcon }
        galleryButtonClickAction = { binding.galleryForeground }
    }

    private fun viewPagerInitConfig() {
        binding.apply {
            val fragments = listOf(
                LiveRecognitionFragment(),
                CaptureImageFragment(),
            )
            val adapter = ScanViewPagerAdapter(fragments, this@ScanTypeFragment)
            scanViewPager.adapter = adapter

            TabLayoutMediator(scanTabLayout, scanViewPager) { tab, position ->
                tab.text = when (position) {
                    1 -> {
                        CameraTypes.LIVE_SCANNER.title
                    }

                    2 -> {
                        CameraTypes.CAPTURE_IMAGE_SCREEN.title
                    }

                    else -> {
                        CameraTypes.QR_CODE_SCREEN.title
                    }
                }
            }.attach()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        var captureButtonClickAction: () -> View? = { null }
        var galleryButtonClickAction: () -> View? = { null }
    }
}