package com.natiqhaciyef.prodocument.ui.view.options.merge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.common.R
import com.natiqhaciyef.common.model.mapped.MappedTokenModel
import com.natiqhaciyef.prodocument.databinding.FragmentMergePdfsBinding
import com.natiqhaciyef.prodocument.ui.base.BaseFragment
import com.natiqhaciyef.prodocument.ui.base.BaseNavigationDeepLink
import com.natiqhaciyef.prodocument.ui.store.AppStorePrefKeys
import com.natiqhaciyef.prodocument.ui.view.main.home.adapter.FileItemAdapter
import com.natiqhaciyef.prodocument.ui.view.options.merge.viewmodel.MergePdfViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MergePdfsFragment : BaseFragment<FragmentMergePdfsBinding, MergePdfViewModel>(
    FragmentMergePdfsBinding::inflate,
    MergePdfViewModel::class
) {
    private val filesList = mutableListOf<MappedMaterialModel>()
    private var adapter: FileItemAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMergePdfsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            setFileListEmptyCheckConfig(filesList)
            setFilesCountConfigurations()

            adapter = FileItemAdapter(requireContext(), filesList, requireContext().getString(R.string.merge_pdf))
            materialsRecyclerView.adapter = adapter

            addMoreFilesButton.setOnClickListener { addFileButtonAction() }
            mergeButton.setOnClickListener { mergeButtonAction() }
            goBackIcon.setOnClickListener { navigateByRouteTitle(BaseNavigationDeepLink.HOME_ROUTE)  }
        }
    }

    private fun setFilesCountConfigurations() {
        lifecycleScope.launch {
            binding.mergeDescriptionText.text =
                getString(R.string.merge_pdf_description, "${filesList.size}")
        }
    }

    private fun getFileTitle() {
        binding.apply {

        }
    }

    private fun setFileListEmptyCheckConfig(list: List<MappedMaterialModel>) {
        binding.materialsRecyclerView.visibility = if (list.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun addFileButtonAction() {
//        filesList.add()
        setFilesCountConfigurations()
        setFileListEmptyCheckConfig(filesList)
        adapter?.updateList(filesList)

    }

    private fun mergeButtonAction() {

    }
}