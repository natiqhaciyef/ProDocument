package com.natiqhaciyef.prodocument.ui.view.main.premium

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.natiqhaciyef.common.model.mapped.MappedSubscriptionModel
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.databinding.FragmentSubscriptionBinding
import com.natiqhaciyef.prodocument.ui.view.main.premium.adapter.FeatureAdapter
import com.natiqhaciyef.prodocument.ui.view.main.premium.contract.PremiumContract
import com.natiqhaciyef.prodocument.ui.view.main.premium.viewmodel.PremiumViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass


@AndroidEntryPoint
class SubscriptionFragment(
    private val subscriptionModel: MappedSubscriptionModel?,
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSubscriptionBinding = FragmentSubscriptionBinding::inflate,
    override val viewModelClass: KClass<PremiumViewModel> = PremiumViewModel::class
) : BaseFragment<FragmentSubscriptionBinding, PremiumViewModel, PremiumContract.PremiumState, PremiumContract.PremiumEvent, PremiumContract.PremiumEffect>() {
    private var adapter: FeatureAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        config()
    }

    override fun onStateChange(state: PremiumContract.PremiumState) {
        when {
            state.isLoading -> {

            }

            else -> {
                if (state.pickedSubscription == subscriptionModel && state.isPicked?.resultCode in 200..299)
                    goToPayment()
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun config() {
        if (subscriptionModel != null) {
            with(binding) {
                val price = "$${subscriptionModel.price}"
                val perTime =
                    "${subscriptionModel.perTime} ${subscriptionModel.timeType.name.lowercase()}"

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    priceText.text =
                        Html.fromHtml(
                            getString(
                                com.natiqhaciyef.common.R.string.subscription_price_text,
                                price,
                                perTime
                            ), Html.FROM_HTML_MODE_COMPACT
                        )
                } else {
                    priceText.text = Html.fromHtml(
                        getString(
                            com.natiqhaciyef.common.R.string.subscription_price_text,
                            price,
                            perTime
                        )
                    )
                }

                adapter = FeatureAdapter(subscriptionModel.features.toMutableList())
                featuresRecyclerView.adapter = adapter
                featuresRecyclerView.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

                descriptionText.text = subscriptionModel.description
                fragmentBackground.setBackgroundColor(com.natiqhaciyef.common.R.color.gradient_red)

                selectPlanButton.setOnClickListener { selectPlanClickEvent(plan = subscriptionModel) }
            }
        }
    }

    private fun goToPayment(){
        // navigate to payment
        Toast.makeText(requireContext(), "Posted", Toast.LENGTH_SHORT).show()
    }

    private fun selectPlanClickEvent(plan: MappedSubscriptionModel){
        viewModel.postEvent(PremiumContract.PremiumEvent.PickPlanEvent(planToken = plan.token))
    }
}