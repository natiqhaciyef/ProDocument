package com.natiqhaciyef.prodocument.ui.view.registration

import android.os.Bundle
import com.natiqhaciyef.prodocument.databinding.ActivityRegistrationBinding
import com.natiqhaciyef.prodocument.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationActivity : BaseActivity<ActivityRegistrationBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}