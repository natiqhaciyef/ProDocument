package com.natiqhaciyef.prodocument.ui.view.main.files

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.common.R
import com.natiqhaciyef.domain.worker.config.startDownloadingFile
import com.natiqhaciyef.prodocument.databinding.FragmentFileBottomSheetOptionBinding
import com.natiqhaciyef.prodocument.ui.manager.CameraManager.Companion.createAndShareFile
import com.natiqhaciyef.prodocument.ui.view.main.MainActivity
import com.natiqhaciyef.prodocument.ui.view.main.profile.model.ParamsUIModel
import com.natiqhaciyef.prodocument.ui.view.main.profile.params.preferences.adapter.ParamsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FileBottomSheetOptionFragment(
    private val material: MappedMaterialModel,
    var list: List<ParamsUIModel>
) : BottomSheetDialogFragment() {
    private var _binding: FragmentFileBottomSheetOptionBinding? = null
    private val binding: FragmentFileBottomSheetOptionBinding
        get() = _binding!!
    private var paramsAdapter: ParamsAdapter? = null

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
                            // success effect
                        },
                        failed = {
                            // fail effect
                        },
                        running = {
                            // loading effect
                        },
                    )
                }

                getString(R.string.export_to) -> {
                    createAndShareFile(material = material, isShare = true)
                }

                getString(R.string.merge_pdf) -> {
                    val action = FilesFragmentDirections.actionFilesFragmentToMergeNavGraph()
                    (parentFragment as FilesFragment).navigate(action)
                }

                else -> {}
            }
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        dialog.dismiss()
    }
}