package com.natiqhaciyef.prodocument.ui.view.options.scan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.ExperimentalGetImage
import com.google.android.material.tabs.TabLayoutMediator
import com.natiqhaciyef.prodocument.databinding.FragmentScanTypeBinding
import com.natiqhaciyef.prodocument.ui.base.BaseFragment
import com.natiqhaciyef.prodocument.ui.base.BaseViewModel
import com.natiqhaciyef.prodocument.ui.view.options.scan.adapter.ScanViewPagerAdapter
import com.natiqhaciyef.prodocument.ui.view.options.scan.behaviour.CameraTypes
import dagger.hilt.android.AndroidEntryPoint


@ExperimentalGetImage
@AndroidEntryPoint
class ScanTypeFragment : BaseFragment<FragmentScanTypeBinding, BaseViewModel>(
    FragmentScanTypeBinding::inflate,
    null
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPagerInitConfig()
        captureButtonClickAction = { binding.captureImageIcon }
        galleryButtonClickAction = { binding.galleryForeground }
    }

    private fun viewPagerInitConfig() {
        binding.apply {
            val fragments = listOf(
                ScanFragment(),
                LiveRecognitionFragment(),
                CaptureImageFragment(),
            )
            val adapter = ScanViewPagerAdapter(fragments, this@ScanTypeFragment)
            scanViewPager.adapter = adapter

            TabLayoutMediator(scanTabLayout, scanViewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> {
                        CameraTypes.QR_CODE_SCREEN.title
                    }

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