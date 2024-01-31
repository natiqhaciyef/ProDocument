package com.natiqhaciyef.prodocument.ui.view.registration

import android.os.Bundle
import com.natiqhaciyef.prodocument.databinding.ActivityRegistrationBinding
import com.natiqhaciyef.prodocument.ui.base.BaseActivity

class RegistrationActivity : BaseActivity() {
    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}