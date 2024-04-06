package com.natiqhaciyef.prodocument.ui.base

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding


abstract class BaseRecyclerViewAdapter<T: Any, VB : ViewBinding>(
    var list: MutableList<T>,
) : RecyclerView.Adapter<BaseRecyclerViewAdapter<T, VB>.BaseViewHolder>() {
    abstract val binding: (Context, ViewGroup, Boolean) -> VB

    inner class BaseViewHolder(var binding: VB, var context: Context) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = binding.invoke(parent.context, parent, false)

        return BaseViewHolder(inflater, parent.context)
    }

    override fun getItemCount(): Int = list.size

    fun updateList(list: MutableList<T>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }
}