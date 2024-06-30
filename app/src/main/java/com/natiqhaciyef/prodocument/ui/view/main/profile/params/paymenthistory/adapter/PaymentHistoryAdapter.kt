package com.natiqhaciyef.prodocument.ui.view.main.profile.params.paymenthistory.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.natiqhaciyef.common.model.payment.PaymentHistoryModel
import com.natiqhaciyef.core.base.ui.BaseRecyclerViewAdapter
import com.natiqhaciyef.common.R
import com.natiqhaciyef.common.constants.FORMATTED_NUMBER_TWO
import com.natiqhaciyef.common.model.Currency
import com.natiqhaciyef.prodocument.databinding.RecyclerPaymentHistoryItemBinding

class PaymentHistoryAdapter(
    private val ctx: Context,
    historyList: MutableList<PaymentHistoryModel>
): BaseRecyclerViewAdapter<PaymentHistoryModel, RecyclerPaymentHistoryItemBinding>(historyList) {
    override val binding: (Context, ViewGroup, Boolean) -> RecyclerPaymentHistoryItemBinding = { ctx, vg, bool ->
        RecyclerPaymentHistoryItemBinding.inflate(LayoutInflater.from(ctx), vg, bool)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = list[position]
        val price = FORMATTED_NUMBER_TWO.format(item.price)
        with(holder.binding){
            backgroundImage.setImageResource(item.icon)
            paymentHistoryTitle.text = ctx.getString(R.string.plan_title_template, item.title)
            paymentHistoryMaskedCardNumber.text = item.maskedCardNumber
            paymentHistoryPrice.text = ctx.getString(R.string.payment_currency, item.currency.sign, price)
        }
    }
}