package com.natiqhaciyef.uikit.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.natiqhaciyef.common.model.payment.MappedPaymentModel
import com.natiqhaciyef.common.model.payment.MappedPaymentModel.Companion.toMappedPick
import com.natiqhaciyef.core.base.ui.BaseRecyclerViewAdapter
import com.natiqhaciyef.uikit.databinding.RecyclerPaymentMethodItemBinding

class PaymentMethodsAdapter(
    list: MutableList<MappedPaymentModel>
) : BaseRecyclerViewAdapter<MappedPaymentModel, RecyclerPaymentMethodItemBinding>(list) {
    override val binding: (Context, ViewGroup, Boolean) -> RecyclerPaymentMethodItemBinding =
        { ctx, parent, _ ->
            RecyclerPaymentMethodItemBinding.inflate(LayoutInflater.from(ctx), parent, false)
        }


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val paymentModel = list[position]
        val paymentMethod = paymentModel.toMappedPick()
        with(holder.binding) {
            paymentImage.setImageResource(paymentMethod.image)

            paymentTitle.setText(paymentMethod.type.resourceId)

            if (paymentMethod.maskedCardNumber.isNotEmpty())
                paymentTitle.text = paymentMethod.maskedCardNumber

            pickPaymentButtonIcon.setOnClickListener { onClickAction.invoke(paymentModel) }
        }

        holder.itemView.setOnClickListener { onClickAction.invoke(paymentModel) }
    }

    var onClickAction: (MappedPaymentModel) -> Unit = {}
}