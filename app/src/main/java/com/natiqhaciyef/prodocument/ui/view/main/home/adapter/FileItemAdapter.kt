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

class FileItemAdapter(
    var list: MutableList<MappedMaterialModel>,
    private val type: String
) : RecyclerView.Adapter<FileItemAdapter.FileItemHolder>() {

    var onClickAction: (String) -> Unit = {

    }

    var removeAction: () -> Unit = {

    }

    inner class FileItemHolder(val binding: RecyclerFilesItemViewBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileItemHolder {
        val binding =
            RecyclerFilesItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FileItemHolder(binding, parent.context)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: FileItemHolder, position: Int) {
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
        view.fileRemoveIcon.setOnClickListener { removeAction.invoke() }
        holder.itemView.setOnClickListener { onClickAction.invoke(file.id) }
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

    fun updateList(updatedList: List<MappedMaterialModel>) {
        this.list = updatedList.toMutableList()
        notifyDataSetChanged()
    }

}