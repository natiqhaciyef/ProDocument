package com.natiqhaciyef.prodocument.ui.view.main

import android.os.Bundle
import com.natiqhaciyef.prodocument.databinding.ActivityMainBinding
import com.natiqhaciyef.prodocument.ui.base.BaseActivity

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}