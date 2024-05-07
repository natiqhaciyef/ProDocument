package com.natiqhaciyef.prodocument.ui.view.main.home.options.split

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.navArgs
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.prodocument.databinding.FragmentMoreInfoSplitBinding
import com.natiqhaciyef.prodocument.ui.base.BaseFragment
import com.natiqhaciyef.prodocument.ui.util.BundleConstants.BUNDLE_LIST_MATERIAL
import com.natiqhaciyef.prodocument.ui.util.BundleConstants.BUNDLE_MATERIAL
import com.natiqhaciyef.prodocument.ui.util.BundleConstants.BUNDLE_TITLE
import com.natiqhaciyef.prodocument.ui.util.BundleConstants.BUNDLE_TYPE
import com.natiqhaciyef.prodocument.ui.view.main.home.options.split.SplitFragment.Companion.SPLIT_TYPE
import com.natiqhaciyef.prodocument.ui.view.main.home.options.split.contract.SplitContract
import com.natiqhaciyef.prodocument.ui.view.main.home.options.split.viewmodel.SplitViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

@AndroidEntryPoint
class MoreInfoSplitFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMoreInfoSplitBinding = FragmentMoreInfoSplitBinding::inflate,
    override val viewModelClass: KClass<SplitViewModel> = SplitViewModel::class
) : BaseFragment<FragmentMoreInfoSplitBinding, SplitViewModel, SplitContract.SplitState, SplitContract.SplitEvent, SplitContract.SplitEffect>() {
    private var bundle = Bundle()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arg: MoreInfoSplitFragmentArgs by navArgs()
        bundle = arg.resourceBundle

        config()
    }

    override fun onStateChange(state: SplitContract.SplitState) {
        when {
            state.isLoading -> {}
            else -> {
                if (state.materialList != null)
                    continueButtonClickAction(state.materialList!!)
            }
        }
    }

    override fun onEffectUpdate(effect: SplitContract.SplitEffect) {

    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun config() {
        with(binding) {
            buttonEnableCheck()
            continueButton.setOnClickListener { continueButtonClickEvent() }
        }
    }

    private fun continueButtonClickAction(list: List<MappedMaterialModel>) {
        // split pdf and get result
        // navigate to modify pdf screen
        bundle.putParcelableArray(BUNDLE_LIST_MATERIAL, list.toTypedArray())
        val action = MoreInfoSplitFragmentDirections.actionMoreInfoSplitFragmentToPreviewMaterialNavGraph(bundle)
        navigate(action)
    }

    private fun continueButtonClickEvent(){
        with(binding){
            val firstLine = fromInput.text.toString()
            val lastLine =
                if (isLastLineOfFile.isChecked) LAST_LINE_NOT_SELECTED else untilToInput.text.toString()
            val title = bundle.getString(BUNDLE_TITLE)
            val material = bundle.getParcelable<MappedMaterialModel>(BUNDLE_MATERIAL)!!

            viewModel.postEvent(SplitContract.SplitEvent.SplitPdfByLinesEvent(
                firstLine = firstLine,
                lastLine = lastLine,
                title = title ?: "",
                material = material
            ))
        }
    }

    private fun buttonEnableCheck(){
        binding.apply {
            fromInput.doOnTextChanged { text, _, _, _ ->
                continueButton.isEnabled = buttonEnableConditions(text)
            }

            untilToInput.doOnTextChanged { text, _, _, _ ->
                continueButton.isEnabled = buttonEnableConditions(text)
            }

            isLastLineOfFile.setOnCheckedChangeListener { _, b ->
                continueButton.isEnabled = buttonEnableConditions(fromInput.text)
                untilToInput.isEnabled = !b
            }
        }
    }


    private fun buttonEnableConditions(text: CharSequence?) =
        !text.isNullOrEmpty() && (binding.untilToInput.text.isNotEmpty() || binding.isLastLineOfFile.isChecked)


    companion object {
        const val LAST_LINE_NOT_SELECTED = "LastLineNotSelected"
    }
}