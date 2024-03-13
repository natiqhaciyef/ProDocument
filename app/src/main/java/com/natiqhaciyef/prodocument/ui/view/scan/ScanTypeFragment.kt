package com.natiqhaciyef.prodocument.ui.view.scan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.ExperimentalGetImage
import com.google.android.material.tabs.TabLayoutMediator
import com.natiqhaciyef.prodocument.databinding.FragmentScanTypeBinding
import com.natiqhaciyef.prodocument.ui.base.BaseFragment
import com.natiqhaciyef.prodocument.ui.view.scan.adapter.ScanViewPagerAdapter
import com.natiqhaciyef.prodocument.ui.view.scan.behaviour.CameraTypes
import dagger.hilt.android.AndroidEntryPoint


@ExperimentalGetImage
@AndroidEntryPoint
class ScanTypeFragment : BaseFragment() {
    private var _binding: FragmentScanTypeBinding? = null
    private val binding: FragmentScanTypeBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScanTypeBinding.inflate(inflater, container, false)
        return binding.root
    }

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