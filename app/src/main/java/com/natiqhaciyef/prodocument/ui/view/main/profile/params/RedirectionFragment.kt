package com.natiqhaciyef.prodocument.ui.view.main.profile.params

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.core.base.ui.UiEvent
import com.natiqhaciyef.core.base.ui.UiState
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.databinding.FragmentRedirectionBinding
import com.natiqhaciyef.prodocument.ui.view.main.profile.contract.ProfileContract
import com.natiqhaciyef.prodocument.ui.view.main.profile.model.Settings
import com.natiqhaciyef.prodocument.ui.view.main.profile.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

@AndroidEntryPoint
class RedirectionFragment : Fragment() {
    private var binding: FragmentRedirectionBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRedirectionBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: RedirectionFragmentArgs by navArgs()
        val navigateOptions = Settings.stringToNavigation(args.settingTitle)
        navigateOptions?.let {
            findNavController().navigate(it)
        }
    }
}