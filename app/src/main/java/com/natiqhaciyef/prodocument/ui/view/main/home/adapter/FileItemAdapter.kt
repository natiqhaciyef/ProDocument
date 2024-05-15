package com.natiqhaciyef.prodocument.ui.view.main.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import coil.load
import com.natiqhaciyef.prodocument.databinding.RecyclerFilesItemViewBinding
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.common.R
import com.natiqhaciyef.core.base.ui.BaseRecyclerViewAdapter
import com.natiqhaciyef.prodocument.ui.custom.CustomMaterialOptionsBottomSheetFragment
import com.natiqhaciyef.core.model.CategoryItem
import com.natiqhaciyef.prodocument.ui.util.CameraReader.Companion.createAndShareFile
import com.natiqhaciyef.prodocument.ui.view.main.MainActivity

class FileItemAdapter(
    dataList: MutableList<MappedMaterialModel>,
    private val type: String,
    private var fragment: Fragment? = null,
    private var context: Context? = null
) : BaseRecyclerViewAdapter<MappedMaterialModel, RecyclerFilesItemViewBinding>(dataList) {
    override val binding: (Context, ViewGroup, Boolean) -> RecyclerFilesItemViewBinding =
        { context, viewGroup, bool ->
            RecyclerFilesItemViewBinding.inflate(LayoutInflater.from(context), viewGroup, bool)
        }

    var onClickAction: (String) -> Unit = {

    }

    var removeAction: (String) -> Unit = {

    }

    private fun showBottomSheetDialog(
        material: MappedMaterialModel,
        shareOptions: List<CategoryItem>
    ) {
        if (fragment != null) {
            CustomMaterialOptionsBottomSheetFragment.list = shareOptions.toMutableList()
            CustomMaterialOptionsBottomSheetFragment { type ->
                shareFile(material.copy(type = type))
            }.show(
                fragment!!.childFragmentManager,
                CustomMaterialOptionsBottomSheetFragment::class.simpleName
            )
        }
    }


    private fun configOfMerge(binding: RecyclerFilesItemViewBinding) {
        binding.fileShareIcon.visibility = View.GONE
        binding.fileOptionMenuIcon.visibility = View.GONE
        binding.fileRemoveIcon.visibility = View.VISIBLE

        val paramsTitle = binding.fileTitleText.layoutParams as ConstraintLayout.LayoutParams
        paramsTitle.endToStart = binding.fileRemoveIcon.id

        val paramsDate = binding.fileTitleText.layoutParams as ConstraintLayout.LayoutParams
        paramsDate.endToStart = binding.fileRemoveIcon.id
    }

    private fun configOfHome(binding: RecyclerFilesItemViewBinding) {
        binding.fileShareIcon.visibility = View.VISIBLE
        binding.fileOptionMenuIcon.visibility = View.VISIBLE
        binding.fileRemoveIcon.visibility = View.GONE

        val paramsTitle = binding.fileTitleText.layoutParams as ConstraintLayout.LayoutParams
        paramsTitle.endToStart = binding.fileShareIcon.id

        val paramsDate = binding.fileTitleText.layoutParams as ConstraintLayout.LayoutParams
        paramsDate.endToStart = binding.fileShareIcon.id
    }


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val view = holder.binding
        val file = list[position]

        when (type) {
            holder.context.getString(R.string.scan_code), holder.context.getString(R.string.default_type) -> {
                configOfHome(view)
            }

            holder.context.getString(R.string.merge_pdf) -> {
                configOfMerge(view)
            }
        }

        view.fileTitleText.text = file.title
        view.fileDateText.text = file.createdDate
        view.filePreviewImage.load(file.image)
        view.fileRemoveIcon.setOnClickListener { removeAction.invoke(file.id) }
        holder.itemView.setOnClickListener { onClickAction.invoke(file.id) }
        view.fileShareIcon.setOnClickListener {
            if (fragment != null && context != null) {
                showBottomSheetDialog(
                    material = file,
                    shareOptions = (fragment!!.requireActivity() as MainActivity).getShareOptionsList(
                        context = context!!
                    )
                )
            }
        }
    }

    private fun shareFile(material: MappedMaterialModel) = fragment?.createAndShareFile(
        material = material,
        isShare = true
    )
}