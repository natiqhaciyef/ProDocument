package com.natiqhaciyef.prodocument.ui.view.main.home.options.e_sign

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.navArgs
import com.natiqhaciyef.common.constants.SOMETHING_WENT_WRONG
import com.natiqhaciyef.common.constants.ZERO
import com.natiqhaciyef.common.helpers.toResponseString
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.databinding.FragmentAddSignBinding
import com.natiqhaciyef.uikit.manager.FileManager.createSafePdfUriLoader
import com.natiqhaciyef.prodocument.ui.util.BUNDLE_MATERIAL
import com.natiqhaciyef.prodocument.ui.util.BUNDLE_SIGN_BITMAP
import com.natiqhaciyef.prodocument.ui.view.main.home.options.e_sign.contract.ESignContract
import com.natiqhaciyef.prodocument.ui.view.main.home.options.e_sign.viewmodel.ESignViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass


@AndroidEntryPoint
class AddSignFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAddSignBinding = FragmentAddSignBinding::inflate,
    override val viewModelClass: KClass<ESignViewModel> = ESignViewModel::class
) : BaseFragment<FragmentAddSignBinding, ESignViewModel, ESignContract.ESignState, ESignContract.ESignEvent, ESignContract.ESignEffect>() {
    private var resBundle = bundleOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arg: AddSignFragmentArgs by navArgs()
        resBundle = arg.resourceBundle

        config()
    }

    override fun onStateChange(state: ESignContract.ESignState) {
        when {
            state.isLoading -> {
                changeVisibilityOfProgressBar(true)
                errorResultConfig(false)
            }

            isIdleState(state) -> {
                changeVisibilityOfProgressBar(false)
                errorResultConfig(true)
            }

            else -> {
                errorResultConfig(false)
                changeVisibilityOfProgressBar(false)
            }
        }
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

    private fun errorResultConfig(isVisible: Boolean = true){
        with(binding){
            notFoundLayout.visibility = if (isVisible) View.VISIBLE else View.GONE
            uiLayout.visibility = if (isVisible) View.GONE else View.VISIBLE

            notFoundDescription.text = getString(com.natiqhaciyef.common.R.string.files_loading_error_description_result)
            notFoundTitle.text = SOMETHING_WENT_WRONG
        }
    }


    private fun config() {
        val material = resBundle.getParcelable<MappedMaterialModel>(BUNDLE_MATERIAL)
        val signBitmap = resBundle.getParcelable<Bitmap>(BUNDLE_SIGN_BITMAP)

        with(binding) {
            pdfView.createSafePdfUriLoader(material?.url)
            signImageView.setImageBitmap(signBitmap)

            signImageTouchListenerConfig(signImageView)
            continueButton.setOnClickListener { continueButtonClickEvent() }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun signImageTouchListenerConfig(imageView: ImageView) {
        var initialX = ZERO.toFloat()
        var initialY = ZERO.toFloat()

        imageView.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialX = event.rawX
                    initialY = event.rawY
                }

                MotionEvent.ACTION_MOVE -> {
                    val deltaX = event.rawX - initialX
                    val deltaY = event.rawY - initialY
                    imageView.x += deltaX
                    imageView.y += deltaY
                    initialX = event.rawX
                    initialY = event.rawY
                }

                else -> {}
            }
            true
        }
    }

    private fun continueButtonClickEvent() {
        with(binding){
            val xPosition = signImageView.x
            val yPosition = signImageView.y

            val pageNumber = pdfView.currentPage

            val eSign = resBundle.getParcelable<Bitmap>(BUNDLE_SIGN_BITMAP) as Bitmap
            val material =
                resBundle.getParcelable<MappedMaterialModel>(BUNDLE_MATERIAL) as MappedMaterialModel

            viewModel.postEvent(
                event = ESignContract.ESignEvent.SignMaterialEvent(
                    material = material,
                    bitmap = eSign,
                    eSign = eSign.toResponseString(),
                    positionsList = mutableListOf(xPosition, yPosition),
                    pageNumber = pageNumber
                )
            )
        }
    }
}