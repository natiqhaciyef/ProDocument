package com.natiqhaciyef.prodocument.ui.view.main.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.natiqhaciyef.common.model.MenuItemModel
import com.natiqhaciyef.prodocument.databinding.RecyclerMenuItemViewBinding


class MenuAdapter(
    private var list: List<MenuItemModel>
) : RecyclerView.Adapter<MenuAdapter.MenuHolder>() {

    var onClickAction: (String) -> Unit = {}

    inner class MenuHolder(val binding: RecyclerMenuItemViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuHolder {
        val binding =
            RecyclerMenuItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MenuHolder, position: Int) {
        val menuItem = list[position]
        val view = holder.binding

        view.menuItemImage.setImageResource(menuItem.imageId)
        view.menuItemTitle.text = menuItem.title

        holder.itemView.setOnClickListener { onClickAction.invoke(menuItem.routeTitle) }
    }


}