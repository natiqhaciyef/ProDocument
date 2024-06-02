package com.natiqhaciyef.prodocument.ui.view.main.payment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.natiqhaciyef.common.model.payment.MappedPaymentPickModel
import com.natiqhaciyef.core.base.ui.BaseRecyclerViewAdapter
import com.natiqhaciyef.prodocument.databinding.RecyclerPaymentMethodItemBinding

class PaymentMethodsAdapter(
    list: MutableList<MappedPaymentPickModel>
) : BaseRecyclerViewAdapter<MappedPaymentPickModel, RecyclerPaymentMethodItemBinding>(list) {
    override val binding: (Context, ViewGroup, Boolean) -> RecyclerPaymentMethodItemBinding =
        { ctx, parent, _ ->
            RecyclerPaymentMethodItemBinding.inflate(LayoutInflater.from(ctx), parent, false)
        }


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val paymentMethod = list[position]
        with(holder.binding) {
            paymentImage.setImageResource(paymentMethod.image)

            paymentTitle.setText(paymentMethod.type.resourceId)

            if (paymentMethod.maskedCardNumber.isNotEmpty())
                paymentTitle.text = paymentMethod.maskedCardNumber

            pickPaymentButtonIcon.setOnClickListener { onClickAction.invoke(paymentMethod) }
        }

        holder.itemView.setOnClickListener { onClickAction.invoke(paymentMethod) }
    }

    var onClickAction: (MappedPaymentPickModel) -> Unit = {}
}