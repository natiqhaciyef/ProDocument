package com.natiqhaciyef.prodocument.ui.view.main.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.natiqhaciyef.prodocument.databinding.RecyclerFilesItemViewBinding
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.common.R
import com.natiqhaciyef.prodocument.ui.base.BaseRecyclerViewAdapter

class FileItemAdapter(
    dataList: MutableList<MappedMaterialModel>,
    private val type: String
) : BaseRecyclerViewAdapter<MappedMaterialModel, RecyclerFilesItemViewBinding>(dataList) {
    override val binding: (Context, ViewGroup, Boolean) -> RecyclerFilesItemViewBinding =
        { context, viewGroup, bool ->
            RecyclerFilesItemViewBinding.inflate(LayoutInflater.from(context), viewGroup, bool)
        }

    var onClickAction: (String) -> Unit = {

    }

    var removeAction: (String) -> Unit = {

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
        println(file)

        when (type) {
            holder.context.getString(R.string.scan_code) -> {
                configOfHome(view)
            }

            holder.context.getString(R.string.merge_pdf) -> {
                configOfMerge(view)
            }
        }

        view.fileTitleText.text = file.title
        view.fileDateText.text = file.createdDate
        Glide.with(holder.context).load(file.image).into(view.filePreviewImage)
        view.fileRemoveIcon.setOnClickListener { removeAction.invoke(file.id) }
        holder.itemView.setOnClickListener { onClickAction.invoke(file.id) }
    }
}