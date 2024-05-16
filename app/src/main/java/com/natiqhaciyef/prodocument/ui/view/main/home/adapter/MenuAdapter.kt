package com.natiqhaciyef.prodocument.ui.view.main.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.natiqhaciyef.common.model.MenuItemModel
import com.natiqhaciyef.prodocument.databinding.RecyclerMenuItemViewBinding
import com.natiqhaciyef.core.base.ui.BaseRecyclerViewAdapter


class MenuAdapter(
    dataList: MutableList<MenuItemModel>
) : BaseRecyclerViewAdapter<MenuItemModel, RecyclerMenuItemViewBinding>(dataList) {

    var onClickAction: (String) -> Unit = {}
    override val binding: (Context, ViewGroup, Boolean) -> RecyclerMenuItemViewBinding =
        { context, viewGroup, b ->
            RecyclerMenuItemViewBinding.inflate(LayoutInflater.from(context), viewGroup, b)
        }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val menuItem = list[position]
        val view = holder.binding

        view.menuItemImage.setImageResource(menuItem.imageId)
        view.menuItemTitle.text = menuItem.title

        holder.itemView.setOnClickListener { onClickAction.invoke(menuItem.routeTitle) }

    }
}