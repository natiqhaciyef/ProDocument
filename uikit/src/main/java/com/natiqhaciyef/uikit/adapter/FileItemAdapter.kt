package com.natiqhaciyef.uikit.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.common.R
import com.natiqhaciyef.common.model.mapped.MappedFolderModel
import com.natiqhaciyef.core.base.ui.BaseRecyclerViewAdapter
import com.natiqhaciyef.uikit.databinding.RecyclerFilesItemViewBinding

class FileItemAdapter(
    dataList: MutableList<Any>,
    private val type: String,
    private var fragment: Fragment? = null,
    private var isActionsAvailable: Boolean = true
) : BaseRecyclerViewAdapter<Any, RecyclerFilesItemViewBinding>(dataList) {
    override val binding: (Context, ViewGroup, Boolean) -> RecyclerFilesItemViewBinding =
        { context, viewGroup, bool ->
            RecyclerFilesItemViewBinding.inflate(LayoutInflater.from(context), viewGroup, bool)
        }

    var onFileClickAction: (MappedMaterialModel) -> Unit = {

    }

    var removeFileAction: (MappedMaterialModel) -> Unit = {

    }

    var optionAction: (MappedMaterialModel) -> Unit = {

    }

    var onFolderClickAction: (MappedFolderModel) -> Unit = {}

    var bottomSheetMaterialDialogOpen: (MappedMaterialModel) -> Unit = {

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

        when (file) {
            is MappedMaterialModel -> {
                if (isActionsAvailable) {
                    fileClickActionAvailable(holder, view, file)
                    holder.itemView.setOnClickListener { onFileClickAction.invoke(file) }
                } else {
                    fileClickActionNotAvailable(view, file)
                    holder.itemView.setOnClickListener { onFileClickAction.invoke(file) }
                }
            }

            is MappedFolderModel -> {
                folderClickAction(view, file)
                holder.itemView.setOnClickListener { onFolderClickAction.invoke(file) }
            }
        }
    }

    private fun folderClickAction(view: RecyclerFilesItemViewBinding, file: MappedFolderModel) {
        with(view.customFilePreview) {
            changeFilesCountVisibility(android.view.View.VISIBLE)
            setCustomTitle(file.title)
            setFilesCount(file.filesCount)
            setCustomDate(file.createdDate)

            if (file.icon != null)
                if (file.icon!!.startsWith("https"))
                    setImageIcon(file.icon!!)
                else
                    setImageIconByName(file.icon!!)
            else
                setImageIcon(R.drawable.folder_filled_icon)
        }
    }

    private fun fileClickActionAvailable(
        holder: BaseViewHolder,
        view: RecyclerFilesItemViewBinding,
        file: MappedMaterialModel
    ) {
        when (type) {
            holder.context.getString(R.string.scan_code), holder.context.getString(R.string.default_type) -> {
                configOfHome(view)
            }

            holder.context.getString(R.string.merge_pdf) -> {
                configOfMerge(view)
            }
        }

        with(view.customFilePreview) {
            changeFilesCountVisibility(View.GONE)
            setCustomTitle(file.title)
            setCustomDate(file.createdDate)
            setImageIcon(file.image)

            addActionToRemove { removeFileAction.invoke(file) }
            addActionToOptions { optionAction.invoke(file) }
            addActionToShare {

                if (fragment != null && context != null) {
                    bottomSheetMaterialDialogOpen.invoke(file)
                }
            }
        }
    }

    private fun fileClickActionNotAvailable(
        view: RecyclerFilesItemViewBinding,
        file: MappedMaterialModel
    ) {
        with(view.customFilePreview) {
            changeFilesCountVisibility(View.GONE)
            setCustomTitle(file.title)
            setCustomDate(file.createdDate)
            setImageIcon(file.image)
        }
    }
}