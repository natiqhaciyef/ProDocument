package com.natiqhaciyef.prodocument.ui.view.main.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.natiqhaciyef.prodocument.databinding.RecyclerFilesItemViewBinding
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.common.R

class FileItemAdapter(
    private val context: Context,
    private var list: List<MappedMaterialModel>,
    private val type: String
) : RecyclerView.Adapter<FileItemAdapter.FileItemHolder>() {

    var onClickAction: (String) -> Unit = {

    }

    var removeAction: () -> Unit = {}

    inner class FileItemHolder(val binding: RecyclerFilesItemViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileItemHolder {
        val binding =
            RecyclerFilesItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FileItemHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: FileItemHolder, position: Int) {
        val view = holder.binding
        val file = list[position]

        view.apply {
            fileTitleText.text = file.title
            fileDateText.text = file.createdDate
            Glide.with(context).load(file.image).into(filePreviewImage)
            fileRemoveIcon.setOnClickListener {
                removeAction.invoke()
                notifyDataSetChanged()
            }
        }
        holder.itemView.setOnClickListener { onClickAction.invoke(file.id) }

        when (type) {
            context.getString(R.string.scan_code) -> {
                configOfHome(view)
            }

            context.getString(R.string.merge_pdf) -> {
                configOfMerge(view)
            }
        }
    }

    private fun configOfMerge(binding: RecyclerFilesItemViewBinding) {
        binding.apply {
            fileShareIcon.visibility = View.GONE
            fileOptionMenuIcon.visibility = View.GONE
            fileRemoveIcon.visibility = View.VISIBLE

            val paramsTitle = fileTitleText.layoutParams as ConstraintLayout.LayoutParams
            paramsTitle.endToStart = fileRemoveIcon.id

            val paramsDate = fileTitleText.layoutParams as ConstraintLayout.LayoutParams
            paramsDate.endToStart = fileRemoveIcon.id
        }
    }

    private fun configOfHome(binding: RecyclerFilesItemViewBinding) {
        binding.apply {
            fileShareIcon.visibility = View.VISIBLE
            fileOptionMenuIcon.visibility = View.VISIBLE
            fileRemoveIcon.visibility = View.GONE

            val paramsTitle = fileTitleText.layoutParams as ConstraintLayout.LayoutParams
            paramsTitle.endToStart = fileShareIcon.id

            val paramsDate = fileTitleText.layoutParams as ConstraintLayout.LayoutParams
            paramsDate.endToStart = fileShareIcon.id
        }
    }

    fun updateList(list: List<MappedMaterialModel>) {
        this.list = list
        notifyDataSetChanged()
    }

}