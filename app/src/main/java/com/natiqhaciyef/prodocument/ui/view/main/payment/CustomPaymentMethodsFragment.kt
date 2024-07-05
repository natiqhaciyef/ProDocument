package com.natiqhaciyef.prodocument.ui.view.main.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.natiqhaciyef.common.model.payment.MappedPaymentModel
import com.natiqhaciyef.core.base.ui.BaseBottomSheetFragment
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.databinding.FragmentCustomPaymentMethodsBinding
import com.natiqhaciyef.uikit.adapter.PaymentMethodsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CustomPaymentMethodsFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCustomPaymentMethodsBinding =
        FragmentCustomPaymentMethodsBinding::inflate,
    val list: List<MappedPaymentModel> = listOf(),
    override var onItemClickAction: (MappedPaymentModel) -> Unit = {}
) : BaseBottomSheetFragment<FragmentCustomPaymentMethodsBinding, MappedPaymentModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PaymentMethodsAdapter(list = list.toMutableList())
        adapter.onClickAction = {
            onItemClickAction.invoke(it)
            this.dismiss()
        }

        with(binding) {
            recyclerPaymentView.adapter = adapter
            recyclerPaymentView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}