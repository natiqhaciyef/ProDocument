package com.natiqhaciyef.uikit.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.natiqhaciyef.common.model.ContactMethods
import com.natiqhaciyef.core.base.ui.BaseRecyclerViewAdapter
import com.natiqhaciyef.uikit.databinding.RecyclerContactItemBinding

class ContactAdapter(
    contactMethods: MutableList<ContactMethods>
): BaseRecyclerViewAdapter<ContactMethods, RecyclerContactItemBinding>(contactMethods) {
    override val binding: (Context, ViewGroup, Boolean) -> RecyclerContactItemBinding
        get() = { ctx, vg, bool ->
            RecyclerContactItemBinding.inflate(LayoutInflater.from(ctx), vg, bool)
        }

    var onClickAction: (ContactMethods) -> Unit = {}

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = list[position]
        with(holder.binding){
            contactImage.setImageResource(item.icon)
            contactTitle.setText(item.title)

            holder.itemView.setOnClickListener { onClickAction.invoke(item) }
        }
    }
}