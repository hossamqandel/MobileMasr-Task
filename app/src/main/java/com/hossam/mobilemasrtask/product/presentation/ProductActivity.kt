package com.hossam.mobilemasrtask.product.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.hossam.mobilemasrtask.R
import com.hossam.mobilemasrtask.databinding.ActivityProductBinding
import com.hossam.mobilemasrtask.util.extension.setSystemBarsPadding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSystemBarsPadding()
    }

}