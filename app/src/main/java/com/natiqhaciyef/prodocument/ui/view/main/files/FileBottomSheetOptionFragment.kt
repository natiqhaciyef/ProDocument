package com.natiqhaciyef.prodocument.ui.view.main.files

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.print.PrintManager
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.natiqhaciyef.common.R
import com.natiqhaciyef.common.constants.HUNDRED
import com.natiqhaciyef.common.constants.NINETY
import com.natiqhaciyef.common.constants.SEVENTY_TWO
import com.natiqhaciyef.common.constants.SIX
import com.natiqhaciyef.common.constants.SIXTY
import com.natiqhaciyef.common.model.ParamsUIModel
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.ui.BaseBottomSheetFragment
import com.natiqhaciyef.domain.worker.config.startDownloadingFile
import com.natiqhaciyef.prodocument.BuildConfig
import com.natiqhaciyef.prodocument.databinding.FragmentFileBottomSheetOptionBinding
import com.natiqhaciyef.prodocument.ui.util.BUNDLE_MATERIAL
import com.natiqhaciyef.prodocument.ui.util.BUNDLE_TYPE
import com.natiqhaciyef.prodocument.ui.util.NavigationUtil
import com.natiqhaciyef.prodocument.ui.util.NavigationUtil.COMPRESS_ROUTE
import com.natiqhaciyef.prodocument.ui.util.NavigationUtil.COMPRESS_TYPE
import com.natiqhaciyef.prodocument.ui.util.NavigationUtil.E_SIGN_ROUTE
import com.natiqhaciyef.prodocument.ui.util.NavigationUtil.E_SIGN_TYPE
import com.natiqhaciyef.prodocument.ui.util.NavigationUtil.PROTECT_ROUTE
import com.natiqhaciyef.prodocument.ui.util.NavigationUtil.PROTECT_TYPE
import com.natiqhaciyef.prodocument.ui.view.main.MainActivity
import com.natiqhaciyef.uikit.adapter.FilePrintAdapter
import com.natiqhaciyef.uikit.adapter.ParamsAdapter
import com.natiqhaciyef.uikit.manager.FileManager.createAndShareFile
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FileBottomSheetOptionFragment(
    private val fragment: Fragment,
    private val material: MappedMaterialModel,
    var list: List<ParamsUIModel>,
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFileBottomSheetOptionBinding =
        FragmentFileBottomSheetOptionBinding::inflate,
    override var onClickAction: () -> Unit = {},
    var moveToFolderClickAction: (MappedMaterialModel) -> Unit = {},
    override var onItemClickAction: (MappedMaterialModel) -> Unit = {},
) : BaseBottomSheetFragment<FragmentFileBottomSheetOptionBinding, MappedMaterialModel>() {
    private var paramsAdapter: ParamsAdapter? = null
    private val resourceBundle: Bundle = bundleOf()
    override var onDismissAndCancelAction = {
        onClickAction.invoke()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customMaterialPreviewConfig()
        recyclerViewConfig()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {

            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { parent ->
                setupRatio(bottomSheetDialog)
            }
        }
        return dialog
    }

    private fun setupRatio(bottomSheetDialog: BottomSheetDialog) {
        val expandedHeight: Int //Height of bottom sheet in expanded state
        val bottomSheet =
            bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet) ?: return

        //Retrieve bottom sheet parameters
        BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_COLLAPSED
        val bottomSheetLayoutParams = bottomSheet.layoutParams
        bottomSheetLayoutParams.height = getBottomSheetDialogDefaultHeight()

        expandedHeight = bottomSheetLayoutParams.height
        val peekHeight =
            (expandedHeight / 1.3) //Peek height to 70% of expanded height (Change based on your view)

        //Setup bottom sheet
        bottomSheet.layoutParams = bottomSheetLayoutParams
        BottomSheetBehavior.from(bottomSheet).skipCollapsed = false
        BottomSheetBehavior.from(bottomSheet).peekHeight = peekHeight.toInt()
        BottomSheetBehavior.from(bottomSheet).isHideable = true

        //OPTIONAL - Setting up recyclerview margins
        val recyclerLayoutParams = binding.recyclerFileActionsView.layoutParams as ConstraintLayout.LayoutParams
        val k = ((SEVENTY_TWO - SIXTY) / SEVENTY_TWO).toFloat()
        recyclerLayoutParams.bottomMargin = (k * SEVENTY_TWO).toInt()
        binding.recyclerFileActionsView.setLayoutParams(recyclerLayoutParams)
    }

    private fun getBottomSheetDialogDefaultHeight(): Int {
        return getWindowHeight()
    }

    private fun getWindowHeight(): Int {
        val displayMetrics = DisplayMetrics()
        fragment.requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }


    private fun customMaterialPreviewConfig() {
        with(binding) {
            customFilePreview.initFilePreview(file = material)
        }
    }

    private fun recyclerViewConfig() {
        paramsAdapter = ParamsAdapter(
            requireContext(),
            list.toMutableList(),
            R.color.white
        )

        with(binding) {
            recyclerFileActionsView.adapter = paramsAdapter
            recyclerFileActionsView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            paramsAdapter?.action = { onCategoryClickAction(it) }
        }
    }

    private fun onCategoryClickAction(param: ParamsUIModel) {
        requireContext().apply {
            when (param.title) {
                getString(R.string.save_to_device) -> {
                    startDownloadingFile(
                        file = material,
                        context = requireContext(),
                        success = {
                            customToastTemplate(it)
                        },
                        failed = {
                            customToastTemplate(it)
                        }
                    )
                }

                getString(R.string.export_to) -> {
                    createAndShareFile(applicationId = BuildConfig.APPLICATION_ID, material = material, isShare = true)
                }

                getString(R.string.add_watermark) -> {
                    activityConfig()
                    val action = FilesFragmentDirections.actionFilesFragmentToWatermarkNavGraph()
                    (parentFragment as FilesFragment).navigate(action)
                }

                getString(R.string.add_digital_signature) -> {
                    activityConfig()
                    resourceBundle.putString(BUNDLE_TYPE, E_SIGN_TYPE)
                    NavigationUtil.navigateByRouteTitle(
                        this@FileBottomSheetOptionFragment,
                        E_SIGN_ROUTE,
                        resourceBundle
                    )
                }

                getString(R.string.split_pdf) -> {
                    activityConfig()
                    val action = FilesFragmentDirections.actionFilesFragmentToScanNavGraph()
                    (parentFragment as FilesFragment).navigate(action)
                }

                getString(R.string.merge_pdf) -> {
                    activityConfig()
                    val action = FilesFragmentDirections.actionFilesFragmentToMergeNavGraph()
                    (parentFragment as FilesFragment).navigate(action)
                }

                getString(R.string.protect_pdf) -> {
                    activityConfig()
                    resourceBundle.putString(BUNDLE_TYPE, PROTECT_TYPE)
                    NavigationUtil.navigateByRouteTitle(
                        this@FileBottomSheetOptionFragment,
                        PROTECT_ROUTE,
                        resourceBundle
                    )
                }

                getString(R.string.compress_pdf) -> {
                    activityConfig()
                    resourceBundle.putString(BUNDLE_TYPE, COMPRESS_TYPE)
                    NavigationUtil.navigateByRouteTitle(
                        this@FileBottomSheetOptionFragment,
                        COMPRESS_ROUTE,
                        resourceBundle
                    )
                }

                getString(R.string.rename) -> {
                    activityConfig()
                    resourceBundle.putParcelable(BUNDLE_MATERIAL, material)
                    val action = FilesFragmentDirections
                        .actionFilesFragmentToPreviewMaterialNavGraph(resourceBundle)
                    (parentFragment as FilesFragment).navigate(action)
                }

                getString(R.string.move_to_folder) -> {
                    moveToFolderClickAction.invoke(material)
                }

                getString(R.string.print) -> {
                    startPrintJob(material)
                }

                getString(R.string.delete) -> {
                    onItemClickAction.invoke(material)
                    dialog?.dismiss()
                }

                else -> {}
            }

        }
    }

    private fun startPrintJob(mappedMaterialModel: MappedMaterialModel) {
        val printManager = requireActivity().getSystemService(Context.PRINT_SERVICE) as PrintManager
        val printAdapter = FilePrintAdapter(requireContext(), mappedMaterialModel)
        printManager.print(getString(R.string.document), printAdapter, null)
    }

    private fun activityConfig(){
        with((fragment.requireActivity() as MainActivity).binding){
            bottomNavBar.visibility = View.GONE
            materialToolbar.visibility = View.GONE
        }
    }

    private fun customToastTemplate(title: String){
        Toast.makeText(requireContext(), title, Toast.LENGTH_SHORT).show()
    }
}