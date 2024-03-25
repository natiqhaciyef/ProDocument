package com.natiqhaciyef.prodocument.ui.view.scan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.natiqhaciyef.prodocument.databinding.RecyclerCategoryItemBinding
import com.natiqhaciyef.prodocument.ui.model.CategoryItem

class ShareCategoryAdapter(
    val list: MutableList<CategoryItem>
) : RecyclerView.Adapter<ShareCategoryAdapter.ShareCategoryHolder>() {

    inner class ShareCategoryHolder(val binding: RecyclerCategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShareCategoryHolder {
        val binding =
            RecyclerCategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShareCategoryHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ShareCategoryHolder, position: Int) {
        val item = list[position]
        with(holder.binding) {
            categoryTitle.text = item.title
            categoryIcon.setImageResource(item.iconId)

            if (item.size != null && !item.sizeType.isNullOrEmpty()) {
                val size = String.format("%.1f", item.size)
                sizeOfItemText.visibility = View.VISIBLE
                sizeOfItemText.text = "$size (${item.sizeType.uppercase()})"
            } else {
                sizeOfItemText.visibility = View.GONE
            }
        }
    }

    fun updateList(list: MutableList<CategoryItem>) {
        this.list.clear()
        this.list.addAll(list)

        notifyDataSetChanged()
    }
}