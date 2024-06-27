package com.natiqhaciyef.prodocument.ui.view.main.profile.params.graphs

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.natiqhaciyef.common.model.MappedGraphDetailModel
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.databinding.FragmentCategoryGraphBinding
import com.natiqhaciyef.prodocument.ui.view.main.MainActivity
import com.natiqhaciyef.prodocument.ui.view.main.profile.contract.ProfileContract
import com.natiqhaciyef.prodocument.ui.view.main.profile.params.graphs.adapter.GraphDetailsAdapter
import com.natiqhaciyef.prodocument.ui.view.main.profile.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

@AndroidEntryPoint
class CategoryGraphFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCategoryGraphBinding = FragmentCategoryGraphBinding::inflate,
    override val viewModelClass: KClass<ProfileViewModel> = ProfileViewModel::class
) : BaseFragment<FragmentCategoryGraphBinding, ProfileViewModel, ProfileContract.ProfileState, ProfileContract.ProfileEvent, ProfileContract.ProfileEffect>() {
    private var adapter: GraphDetailsAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityConfig()
        viewModel.postEvent(ProfileContract.ProfileEvent.GetUserStatistics)
    }

    override fun onStateChange(state: ProfileContract.ProfileState) {
        when {
            state.isLoading -> {}

            else -> {
                if (state.userStatistics != null)
                    recyclerViewConfig(statistics = state.userStatistics!!)
            }
        }
    }

    override fun onEffectUpdate(effect: ProfileContract.ProfileEffect) {

    }

    private fun activityConfig() {
        (activity as MainActivity).also {
            with(it.binding) {
                bottomNavBar.visibility = View.GONE
                materialToolbar.apply {
                    navigationIcon = null
                    visibility = View.VISIBLE
                    setTitleToolbar(getString(com.natiqhaciyef.common.R.string.category_graph))
                    changeVisibility(View.VISIBLE)
                    changeAppIcon(com.natiqhaciyef.common.R.drawable.back_arrow_icon) {
                        onToolbarBackPressAction(bottomNavBar)
                    }
                    appIconVisibility(View.VISIBLE)
                    setVisibilitySearch(View.GONE)
                    setVisibilityOptionsMenu(View.VISIBLE)
                    setIconToOptions(com.natiqhaciyef.common.R.drawable.options_icon)
                    setVisibilityToolbar(View.VISIBLE)
                }
            }
        }
    }

    private fun onToolbarBackPressAction(bnw: BottomNavigationView) {
        bnw.visibility = View.VISIBLE
        navigate(R.id.profileFragment)
    }

    private fun recyclerViewConfig(statistics: List<MappedGraphDetailModel>) {
        adapter = GraphDetailsAdapter(requireContext(), statistics.toMutableList())

        with(binding) {
            recyclerCategoriesView.adapter = adapter
            recyclerCategoriesView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        pieChartConfig(statistics)
    }

    private fun pieChartConfig(statistics: List<MappedGraphDetailModel>) {
        with(binding) {
            val entries = mutableListOf<PieEntry>()
            val colors = mutableListOf<Int>()

            for (stat in statistics){
                entries.add(PieEntry(stat.percentage.toFloat()))
                colors.add(requireContext().getColor(stat.backgroundColor))
            }

            pieChart.setUsePercentValues(true)
            pieChart.description.isEnabled = false
            pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

            pieChart.setDragDecelerationFrictionCoef(0.95f)
            pieChart.isDrawHoleEnabled = true
            pieChart.setHoleColor(requireContext().getColor(com.natiqhaciyef.common.R.color.transparent))
            pieChart.setTransparentCircleColor(requireContext().getColor(com.natiqhaciyef.common.R.color.white))
            pieChart.setTransparentCircleAlpha(110)

            pieChart.holeRadius = 58f
            pieChart.transparentCircleRadius = 61f
            pieChart.setDrawCenterText(true)
            pieChart.setRotationAngle(0f)
            pieChart.isRotationEnabled = true
            pieChart.isHighlightPerTapEnabled = true

            pieChart.animateY(1400, Easing.EaseInOutQuad)
            pieChart.legend.isEnabled = false
            pieChart.setEntryLabelColor(requireContext().getColor(com.natiqhaciyef.common.R.color.white))
            pieChart.setEntryLabelTextSize(12f)

            val dataSet = PieDataSet(entries, "Statistics")
            dataSet.setDrawValues(false)

            dataSet.sliceSpace = 3f
            dataSet.iconsOffset = MPPointF(0f, 40f)
            dataSet.selectionShift = 5f
            dataSet.colors = colors

            val data = PieData(dataSet)
            data.setValueFormatter(PercentFormatter())
            data.setValueTextSize(16f)

            pieChart.setData(data)
            pieChart.highlightValues(null)
            pieChart.invalidate()
        }
    }
}