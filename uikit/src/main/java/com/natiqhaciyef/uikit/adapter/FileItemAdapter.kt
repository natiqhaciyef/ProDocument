package com.natiqhaciyef.uikit.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.common.R
import com.natiqhaciyef.core.base.ui.BaseRecyclerViewAdapter
import com.natiqhaciyef.uikit.databinding.RecyclerFilesItemViewBinding

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

    var onClickAction: (MappedMaterialModel) -> Unit = {

    }

    var removeAction: (MappedMaterialModel) -> Unit = {

    }

    var optionAction: (MappedMaterialModel) -> Unit = {

    }

    var bottomSheetDialogOpen: (MappedMaterialModel) -> Unit = {

    }

    private fun configOfMerge(binding: RecyclerFilesItemViewBinding) {
        with(binding.customFilePreview) {
            changeBothIconsVisibility(View.GONE)
            changeRemoveIconVisibility(View.VISIBLE)
        }
    }

    private fun configOfHome(binding: RecyclerFilesItemViewBinding) {
        with(binding.customFilePreview) {
            changeBothIconsVisibility(View.VISIBLE)
            changeRemoveIconVisibility(View.GONE)
        }
    }


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val view = holder.binding
        val file = list[position]
        view.customFilePreview.bind()

        when (type) {
            holder.context.getString(R.string.scan_code), holder.context.getString(R.string.default_type) -> {
                configOfHome(view)
            }

            holder.context.getString(R.string.merge_pdf) -> {
                configOfMerge(view)
            }
        }

        with(view){
            customFilePreview.setCustomTitle(file.title)
            customFilePreview.setCustomDate(file.createdDate)
            customFilePreview.setImageIcon(file.image)

            customFilePreview.addActionToRemove { removeAction.invoke(file) }
            customFilePreview.addActionToOptions { optionAction.invoke(file) }
            customFilePreview.addActionToShare {

                if (fragment != null && context != null) {
                    bottomSheetDialogOpen.invoke(file)
                }
            }
        }
        holder.itemView.setOnClickListener { onClickAction.invoke(file) }
    }
}