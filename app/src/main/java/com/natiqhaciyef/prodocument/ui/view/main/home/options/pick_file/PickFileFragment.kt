package com.natiqhaciyef.prodocument.ui.view.main.home.options.pick_file

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.navigation.fragment.navArgs
import coil.load
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.databinding.FragmentPickFileBinding
import com.natiqhaciyef.prodocument.ui.manager.FileManager
import com.natiqhaciyef.prodocument.ui.util.BaseNavigationDeepLink
import com.natiqhaciyef.prodocument.ui.util.BaseNavigationDeepLink.COMPRESS_TYPE
import com.natiqhaciyef.prodocument.ui.util.BaseNavigationDeepLink.PROTECT_TYPE
import com.natiqhaciyef.prodocument.ui.util.BaseNavigationDeepLink.SPLIT_TYPE
import com.natiqhaciyef.prodocument.ui.util.BundleConstants
import com.natiqhaciyef.prodocument.ui.util.BundleConstants.BUNDLE_TYPE
import com.natiqhaciyef.prodocument.ui.view.main.home.options.pick_file.contract.PickFileContract
import com.natiqhaciyef.prodocument.ui.view.main.home.options.pick_file.viewmodel.PickFileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

@AndroidEntryPoint
class PickFileFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPickFileBinding = FragmentPickFileBinding::inflate,
    override val viewModelClass: KClass<PickFileViewModel> = PickFileViewModel::class
) : BaseFragment<FragmentPickFileBinding, PickFileViewModel, PickFileContract.PickFileState, PickFileContract.PickFileEvent, PickFileContract.PickFileEffect>() {
    private var resBundle = bundleOf()
    private val fileRequestLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let { intent ->
                    if (intent.data != null)
                        FileManager.readAndCreateFile(
                            activity = requireActivity(),
                            uri = intent.data!!
                        ) { file ->
                            val title = binding.usernamePickFileInput.text.toString()
                            fileConfig(file)
                            binding.continueButton.isEnabled = true
                            binding.continueButton.setOnClickListener {
                                continueButtonAction(
                                    file,
                                    title
                                )
                            }
                        }
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        config()
    }

    private fun config() {
        val args: PickFileFragmentArgs by navArgs()
        resBundle = args.resourceBundle
        typeConfig(resBundle.getString(BUNDLE_TYPE) ?: "")
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

    private fun continueButtonAction(materialModel: MappedMaterialModel, title: String) {
        resBundle.putParcelable(BundleConstants.BUNDLE_MATERIAL, materialModel)
        resBundle.putString(BundleConstants.BUNDLE_TITLE, title)

        val action = when (resBundle.getString(BUNDLE_TYPE)) {
            COMPRESS_TYPE -> {
                PickFileFragmentDirections
                    .actionPickFileFragmentToCompressFragment(resBundle)
            }

            SPLIT_TYPE -> {
                PickFileFragmentDirections
                    .actionPickFileFragmentToMoreInfoSplitFragment(resBundle)
            }

            else -> {
                PickFileFragmentDirections
                    .actionPickFileFragmentToAddPasswordFileFragment(resBundle)
            }
        }

        navigate(action)
    }

    private fun fileRemoveClickAction() {
        binding.apply {
            filePreviewObject.visibility = View.GONE
            continueButton.isEnabled = false
            addFileButton.visibility = View.VISIBLE
        }
    }

    private fun typeConfig(type: String) = when(type){
        COMPRESS_TYPE -> {
            binding.pickFileTitleText.setText(com.natiqhaciyef.common.R.string.compress_pdf)
        }

        SPLIT_TYPE -> {
            binding.pickFileTitleText.setText(com.natiqhaciyef.common.R.string.split_pdf)
        }

        PROTECT_TYPE -> {
            binding.pickFileTitleText.setText(com.natiqhaciyef.common.R.string.protect_pdf)
        }

        else -> {}
    }

    private fun goBackIconClickAction() {
        BaseNavigationDeepLink.navigateByRouteTitle(
            this@PickFileFragment,
            BaseNavigationDeepLink.HOME_ROUTE
        )
    }
}
