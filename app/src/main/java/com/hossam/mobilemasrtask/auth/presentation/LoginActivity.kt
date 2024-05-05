package com.hossam.mobilemasrtask.auth.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.hossam.mobilemasrtask.R
import com.hossam.mobilemasrtask.auth.presentation.utl.LoginState
import com.hossam.mobilemasrtask.databinding.ActivityLoginBinding
import com.hossam.mobilemasrtask.product.presentation.ProductActivity
import com.hossam.mobilemasrtask.util.extension.setSystemBarsPadding
import com.hossam.mobilemasrtask.util.extension.startActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSystemBarsPadding()

        onLoginEvent()
    }

    private fun onLoginEvent(){
        binding.btnLogin.setOnClickListener {
            lifecycleScope.launch {
                startAuthentication()
                observeLoginState()
            }
        }

    }

    private fun startAuthentication(){
        with(binding){
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            viewModel.getAuthState(email = email, password = password)
        }
    }
    
    private suspend fun observeLoginState(){
        lifecycleScope.launch { 
            viewModel.state.collectLatest { state -> 
                when(state){
                    is LoginState.Loading -> binding.progressBar.isVisible = state.isLoading
                    is LoginState.Success -> {
                        startActivity<ProductActivity>().also {
                            finish()
                        }
                    }
                    is LoginState.Error -> {
                        Toast.makeText(
                            this@LoginActivity,
                            getString(state.message),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }



}