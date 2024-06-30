package com.natiqhaciyef.prodocument.ui.view.main.files

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.common.R
import com.natiqhaciyef.domain.worker.config.startDownloadingFile
import com.natiqhaciyef.prodocument.databinding.FragmentFileBottomSheetOptionBinding
import com.natiqhaciyef.prodocument.ui.manager.FileManager.createAndShareFile
import com.natiqhaciyef.prodocument.ui.manager.NavigationManager
import com.natiqhaciyef.prodocument.ui.manager.NavigationManager.COMPRESS_ROUTE
import com.natiqhaciyef.prodocument.ui.manager.NavigationManager.COMPRESS_TYPE
import com.natiqhaciyef.prodocument.ui.manager.NavigationManager.E_SIGN_ROUTE
import com.natiqhaciyef.prodocument.ui.manager.NavigationManager.E_SIGN_TYPE
import com.natiqhaciyef.prodocument.ui.manager.NavigationManager.PROTECT_ROUTE
import com.natiqhaciyef.prodocument.ui.manager.NavigationManager.PROTECT_TYPE
import com.natiqhaciyef.prodocument.ui.util.BUNDLE_MATERIAL
import com.natiqhaciyef.prodocument.ui.util.BUNDLE_TYPE
import com.natiqhaciyef.prodocument.ui.view.main.profile.model.ParamsUIModel
import com.natiqhaciyef.prodocument.ui.view.main.profile.params.preferences.adapter.ParamsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FileBottomSheetOptionFragment(
    private val material: MappedMaterialModel,
    var list: List<ParamsUIModel>,
    var removeEvent: (MappedMaterialModel) -> Unit = {}
) : BottomSheetDialogFragment() {
    private var _binding: FragmentFileBottomSheetOptionBinding? = null
    private val binding: FragmentFileBottomSheetOptionBinding
        get() = _binding!!
    private var paramsAdapter: ParamsAdapter? = null
    private val resourceBundle: Bundle = bundleOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFileBottomSheetOptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customMaterialPreviewConfig()
        recyclerViewConfig()
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
                    createAndShareFile(material = material, isShare = true)
                }

                getString(R.string.add_watermark) -> {
                    val action = FilesFragmentDirections.actionFilesFragmentToWatermarkNavGraph()
                    (parentFragment as FilesFragment).navigate(action)
                }

                getString(R.string.add_digital_signature) -> {
                    resourceBundle.putString(BUNDLE_TYPE, E_SIGN_TYPE)
                    NavigationManager.navigateByRouteTitle(
                        this@FileBottomSheetOptionFragment,
                        E_SIGN_ROUTE,
                        resourceBundle
                    )
                }

                getString(R.string.split_pdf) -> {
                    val action = FilesFragmentDirections.actionFilesFragmentToScanNavGraph()
                    (parentFragment as FilesFragment).navigate(action)
                }

                getString(R.string.merge_pdf) -> {
                    val action = FilesFragmentDirections.actionFilesFragmentToMergeNavGraph()
                    (parentFragment as FilesFragment).navigate(action)
                }

                getString(R.string.protect_pdf) -> {
                    resourceBundle.putString(BUNDLE_TYPE, PROTECT_TYPE)
                    NavigationManager.navigateByRouteTitle(
                        this@FileBottomSheetOptionFragment,
                        PROTECT_ROUTE,
                        resourceBundle
                    )
                }

                getString(R.string.compress_pdf) -> {
                    resourceBundle.putString(BUNDLE_TYPE, COMPRESS_TYPE)
                    NavigationManager.navigateByRouteTitle(
                        this@FileBottomSheetOptionFragment,
                        COMPRESS_ROUTE,
                        resourceBundle
                    )
                }

                getString(R.string.rename) -> {
                    resourceBundle.putParcelable(BUNDLE_MATERIAL, material)
                    val action = FilesFragmentDirections
                        .actionFilesFragmentToPreviewMaterialNavGraph(resourceBundle)
                    (parentFragment as FilesFragment).navigate(action)
                }

                getString(R.string.print) -> {
                    // INSERT: print screen
                }

                getString(R.string.delete) -> {
                    removeEvent.invoke(material)
                }

                else -> {}
            }
        }
    }

    private fun customToastTemplate(title: String){
        Toast.makeText(requireContext(), title, Toast.LENGTH_SHORT).show()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        dialog.dismiss()
    }
}