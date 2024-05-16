package com.natiqhaciyef.prodocument.ui.view.main.home.options.split

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import coil.load
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.prodocument.databinding.FragmentSplitBinding
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.ui.manager.FileManager
import com.natiqhaciyef.prodocument.ui.util.BaseNavigationDeepLink
import com.natiqhaciyef.prodocument.ui.util.BaseNavigationDeepLink.navigateByRouteTitle
import com.natiqhaciyef.prodocument.ui.util.BundleConstants.BUNDLE_MATERIAL
import com.natiqhaciyef.prodocument.ui.util.BundleConstants.BUNDLE_TITLE
import com.natiqhaciyef.prodocument.ui.util.BundleConstants.BUNDLE_TYPE
import com.natiqhaciyef.prodocument.ui.view.main.home.options.split.contract.SplitContract
import com.natiqhaciyef.prodocument.ui.view.main.home.options.split.viewmodel.SplitViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

@AndroidEntryPoint
class SplitFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSplitBinding = FragmentSplitBinding::inflate,
    override val viewModelClass: KClass<SplitViewModel> = SplitViewModel::class
) : BaseFragment<FragmentSplitBinding, SplitViewModel, SplitContract.SplitState, SplitContract.SplitEvent, SplitContract.SplitEffect>() {
    private val bundle = bundleOf()
    private val fileRequestLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let { intent ->
                    if (intent.data != null) {
                        FileManager.readAndCreateFile(
                            activity = requireActivity(),
                            uri = intent.data!!
                        ) { file ->
                            fileConfig(file)
                            binding.continueButton.isEnabled = true
                            binding.continueButton.setOnClickListener { continueButtonAction(file) }
                        }
                    }
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        config()
    }

    override fun onStateChange(state: SplitContract.SplitState) {
        when {
            state.isLoading -> {}
            else -> {}
        }
    }

    private fun config() {
        with(binding) {
            addFileButton.setOnClickListener { FileManager.getFile(fileRequestLauncher) }
            goBackIcon.setOnClickListener { goBackIconClickAction() }
        }
    }


    private fun fileConfig(file: MappedMaterialModel) {
        with(binding) {
            filePreviewImage.load(file.image)
            fileTitleText.text = file.title
            fileDateText.text = file.createdDate

            fileRemoveIcon.setOnClickListener { fileRemoveClickAction() }
            filePreviewObject.visibility = View.VISIBLE
            addFileButton.visibility = View.GONE
        }
    }

    private fun continueButtonAction(materialModel: MappedMaterialModel) {
        val title = binding.usernameSplitInput.text.toString()
        bundle.putParcelable(BUNDLE_MATERIAL, materialModel)
        bundle.putString(BUNDLE_TYPE, SPLIT_TYPE)
        bundle.putString(BUNDLE_TITLE, title)
        val action = SplitFragmentDirections.actionSplitFragmentToMoreInfoSplitFragment(bundle)
        navigate(action)
    }

    private fun fileRemoveClickAction() {
        binding.apply {
            filePreviewObject.visibility = View.GONE
            continueButton.isEnabled = false
            addFileButton.visibility = View.VISIBLE
        }
    }

    private fun goBackIconClickAction(){
        navigateByRouteTitle(this@SplitFragment, BaseNavigationDeepLink.HOME_ROUTE)
    }

    companion object{ const val SPLIT_TYPE = "SplitType" }
}