package com.natiqhaciyef.uikit.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.natiqhaciyef.common.model.MenuItemModel
import com.natiqhaciyef.core.base.ui.BaseRecyclerViewAdapter
import com.natiqhaciyef.uikit.databinding.RecyclerMenuItemViewBinding


class MenuAdapter(
    dataList: MutableList<MenuItemModel>
) : BaseRecyclerViewAdapter<MenuItemModel, RecyclerMenuItemViewBinding>(dataList) {

    var onClickAction: (String, String?) -> Unit = { route, type ->  }
    override val binding: (Context, ViewGroup, Boolean) -> RecyclerMenuItemViewBinding =
        { context, viewGroup, b ->
            RecyclerMenuItemViewBinding.inflate(LayoutInflater.from(context), viewGroup, b)
        }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val menuItem = list[position]
        val view = holder.binding

        view.menuItemImage.setImageResource(menuItem.imageId)
        view.menuItemTitle.text = menuItem.title

        holder.itemView.setOnClickListener { onClickAction.invoke(menuItem.routeTitle, menuItem.type) }

    }
}