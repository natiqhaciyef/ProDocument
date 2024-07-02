package com.natiqhaciyef.prodocument.ui.view.main.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.natiqhaciyef.common.model.payment.MappedPaymentModel
import com.natiqhaciyef.prodocument.databinding.FragmentCustomPaymentMethodsBinding
import com.natiqhaciyef.uikit.adapter.PaymentMethodsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CustomPaymentMethodsFragment(
    val list: List<MappedPaymentModel> = listOf(),
    private var paymentMethodClickAction: (MappedPaymentModel) -> Unit = {}
) : BottomSheetDialogFragment() {
    private var _binding: FragmentCustomPaymentMethodsBinding? = null
    private val binding: FragmentCustomPaymentMethodsBinding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCustomPaymentMethodsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PaymentMethodsAdapter(list = list.toMutableList())
        adapter.onClickAction = {
            paymentMethodClickAction.invoke(it)
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