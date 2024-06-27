package com.natiqhaciyef.prodocument.ui.view.main.profile.params.aboutproscan.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.natiqhaciyef.common.model.ProscanSectionModel
import com.natiqhaciyef.core.base.ui.BaseRecyclerViewAdapter
import com.natiqhaciyef.prodocument.databinding.RecyclerSectionItemBinding

class SectionsAdapter(
    sectionsList: MutableList<ProscanSectionModel>,
) : BaseRecyclerViewAdapter<ProscanSectionModel, RecyclerSectionItemBinding>(sectionsList) {
    override val binding: (Context, ViewGroup, Boolean) -> RecyclerSectionItemBinding
        get() = { ctx, vg, bool ->
            RecyclerSectionItemBinding.inflate(LayoutInflater.from(ctx), vg, bool)
        }

    var onClick: (ProscanSectionModel) -> Unit = {}

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = list[position]

        with(holder) {
            binding.sectionTitle.text = item.title
            itemView.setOnClickListener { onClick.invoke(item) }
        }
    }
}