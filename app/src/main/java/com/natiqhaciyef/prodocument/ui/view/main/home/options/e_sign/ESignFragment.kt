package com.natiqhaciyef.prodocument.ui.view.main.home.options.e_sign

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.navArgs
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.databinding.FragmentESignBinding
import com.natiqhaciyef.prodocument.ui.util.BundleConstants.BUNDLE_MATERIAL
import com.natiqhaciyef.prodocument.ui.view.main.home.options.e_sign.contract.ESignContract
import com.natiqhaciyef.prodocument.ui.view.main.home.options.e_sign.viewmodel.ESignViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass


@AndroidEntryPoint
class ESignFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentESignBinding = FragmentESignBinding::inflate,
    override val viewModelClass: KClass<ESignViewModel> = ESignViewModel::class
) : BaseFragment<FragmentESignBinding, ESignViewModel, ESignContract.ESignState, ESignContract.ESignEvent, ESignContract.ESignEffect>() {
    private var resBundle = bundleOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: ESignFragmentArgs by navArgs()
        resBundle = args.resourceBundle

        binding.continueButton.setOnClickListener {
            sendSignEvent()
        }
    }

    override fun onStateChange(state: ESignContract.ESignState) {
        when {
            state.isLoading -> {

            }

            else -> {

            }
        }
    }

    override fun onEffectUpdate(effect: ESignContract.ESignEffect) {

    }

    private fun sendSignEvent() {
        val material =
            resBundle.getParcelable(BUNDLE_MATERIAL, MappedMaterialModel::class.java) ?: return

        viewModel.postEvent(
            ESignContract.ESignEvent.SignMaterialEvent(
                material = material,
                eSign = "title",
                bitmap = getDrawingBitmap()
            )
        )
    }

    private fun getDrawingBitmap(): Bitmap {
        val drawView = binding.customCanvasDrawView
        // Create a Bitmap with the same size as the DrawView
        val bitmap = Bitmap.createBitmap(drawView.width, drawView.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawView.draw(canvas) // Draw the DrawView's content onto the Bitmap
        return bitmap
    }
}