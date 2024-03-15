package com.natiqhaciyef.prodocument.ui.view.main.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.natiqhaciyef.prodocument.databinding.RecyclerFilesItemViewBinding
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel

class FileItemAdapter(
    private val context: Context,
    private var list: List<MappedMaterialModel>
) : RecyclerView.Adapter<FileItemAdapter.FileItemHolder>() {

    var onClickAction: (String) -> Unit = {

    }

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
        }

        holder.itemView.setOnClickListener { onClickAction.invoke(file.id) }
    }

    fun updateList(list: List<MappedMaterialModel>) {
        this.list = list
        notifyDataSetChanged()
    }

}