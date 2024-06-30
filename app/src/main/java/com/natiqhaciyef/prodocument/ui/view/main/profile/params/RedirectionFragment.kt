package com.natiqhaciyef.prodocument.ui.view.main.profile.params

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.natiqhaciyef.prodocument.databinding.FragmentRedirectionBinding
import com.natiqhaciyef.common.model.Settings
import com.natiqhaciyef.prodocument.ui.view.main.profile.ProfileFragment.Companion.stringToNavigation
import dagger.hilt.android.AndroidEntryPoint

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
        val navigateOptions = stringToNavigation(args.settingTitle)
        navigateOptions?.let {
            findNavController().navigate(it)
        }
    }
}