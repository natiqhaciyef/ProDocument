package com.natiqhaciyef.prodocument.ui.view.main.profile.params.helpcenter

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.natiqhaciyef.common.constants.EMPTY_STRING
import com.natiqhaciyef.common.constants.TWENTY_FOUR
import com.natiqhaciyef.common.model.CategoryModel
import com.natiqhaciyef.common.model.FaqModel
import com.natiqhaciyef.common.model.QuestionCategories
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.core.base.ui.BaseRecyclerHolderStatefulFragment
import com.natiqhaciyef.prodocument.databinding.FragmentFaqBinding
import com.natiqhaciyef.prodocument.ui.view.main.profile.contract.ProfileContract
import com.natiqhaciyef.uikit.adapter.CategoryAdapter
import com.natiqhaciyef.uikit.adapter.FaqAdapter
import com.natiqhaciyef.prodocument.ui.view.main.profile.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

@AndroidEntryPoint
class FAQFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFaqBinding = FragmentFaqBinding::inflate,
    override val viewModelClass: KClass<ProfileViewModel> = ProfileViewModel::class
) : BaseRecyclerHolderStatefulFragment<
        FragmentFaqBinding, ProfileViewModel,FaqModel, FaqAdapter,
        ProfileContract.ProfileState, ProfileContract.ProfileEvent, ProfileContract.ProfileEffect>() {
    override var adapter: FaqAdapter? = null
    private var categoryAdapter: CategoryAdapter? = null
    private var baseList: MutableList<FaqModel>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.postEvent(ProfileContract.ProfileEvent.GetFaqList)
        viewModel.postEvent(ProfileContract.ProfileEvent.GetFaqCategories)
        searchConfig()
    }

    override fun onStateChange(state: ProfileContract.ProfileState) {
        when {
            state.isLoading -> changeVisibilityOfProgressBar(true)

            else -> {
                changeVisibilityOfProgressBar()
                if (state.faqList != null) {
                    baseList = state.faqList?.toMutableList()
                    recyclerViewConfig(state.faqList!!)
                }

                if (state.faqCategoryList != null) {
                    categoryRecyclerConfig(state.faqCategoryList!!)
                }
            }
        }
    }

    override fun onEffectUpdate(effect: ProfileContract.ProfileEffect) {

    }

    private fun changeVisibilityOfProgressBar(isVisible: Boolean = false) {
        if (isVisible) {
            binding.apply {
                uiLayout.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
                progressBar.isIndeterminate = true
            }
        } else {
            binding.apply {
                uiLayout.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                progressBar.isIndeterminate = false
            }
        }
    }


    private fun categoryRecyclerConfig(list: List<CategoryModel>) {
        with(binding) {
            categoryAdapter = CategoryAdapter(requireContext(), list.toMutableList())
            recyclerCategoriesView.adapter = categoryAdapter
            recyclerCategoriesView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    override fun recyclerViewConfig(list: List<FaqModel>){
        val mList = baseList?.toMutableList() ?: mutableListOf()
        with(binding) {
            adapter = FaqAdapter(mList)
            recyclerFaqView.adapter = adapter
            recyclerFaqView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            searchQuestion()
            categoryAdapter?.onClick = { filterList(it.title, baseList!!, true) }
        }
    }

    private fun searchConfig() {
        val search =
            binding.searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_button)
        search?.apply {
            layoutParams.width = TWENTY_FOUR
            layoutParams.height = TWENTY_FOUR
            imageTintList =
                ColorStateList.valueOf(resources.getColor(com.natiqhaciyef.common.R.color.grayscale_500))
        }
    }

    private fun searchQuestion() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newTet: String?): Boolean {
                filterList(newTet?.lowercase() ?: EMPTY_STRING, baseList ?: mutableListOf())
                return true
            }
        })
    }

    private fun filterList(
        text: String,
        faqList: MutableList<FaqModel>,
        isCategory: Boolean = false
    ) {
        val resultList = faqList.filter {
            if (isCategory) it.category == text else it.title.contains(text)
        }


        binding.notFoundLayout.visibility =
            if (resultList.isEmpty()) View.VISIBLE else View.GONE

        if (isCategory && text == QuestionCategories.ALL.title)
            adapter?.updateList(baseList!!)
        else
            adapter?.updateList(resultList.toMutableList())
    }
}