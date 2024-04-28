package com.natiqhaciyef.prodocument.ui.view.main.home.options.watermark

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore.Images.Media
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import coil.load
import com.natiqhaciyef.common.helpers.loadImage
import com.natiqhaciyef.prodocument.databinding.FragmentAddImageWatermarkBinding
import com.natiqhaciyef.prodocument.ui.custom.CustomWatermarkAdderBottomSheetFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddImageWatermarkFragment : Fragment() {
    private var _binding: FragmentAddImageWatermarkBinding? = null
    private val binding: FragmentAddImageWatermarkBinding
        get() = _binding!!

    private var imageUrl: Uri? = null
    private var permissionResultLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                actionResultLauncher.launch(permissionGrantedAction())
            }
        }
    private var actionResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                imageUrl = result.data?.data
                CustomWatermarkAdderBottomSheetFragment.getImageClickAction = { imageUrl.toString() }
                imageViewConfig(imageUrl.toString())
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddImageWatermarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        config()
    }

    private fun config() {
        with(binding) {
            addMoreImagesButton.setOnClickListener { checkPermission() }
            addWatermarkImageView.visibility = View.GONE
            addMoreImagesButton.visibility = View.VISIBLE
        }
    }

    private fun imageViewConfig(image: String) {
        with(binding) {
            addWatermarkImageView.visibility = View.VISIBLE
            addMoreImagesButton.visibility = View.GONE

            addWatermarkImageView.load(image)
        }
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            actionResultLauncher.launch(permissionGrantedAction())
        } else {
            permissionResultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun permissionGrantedAction(): Intent {
        return Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI)
    }
}